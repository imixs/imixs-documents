package org.imixs.workflow.office.wiki;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.ItemCollectionComparator;
import org.imixs.workflow.engine.DocumentService;
import org.imixs.workflow.engine.WorkflowService;
import org.imixs.workflow.exceptions.QueryException;
import org.imixs.workflow.faces.data.WorkflowController;
import org.imixs.workflow.faces.data.WorkflowEvent;
import org.imixs.workflow.faces.fileupload.FileUploadController;
import org.imixs.workflow.office.views.BoardController;
import org.imixs.workflow.office.views.SearchController;

@Named
@ViewScoped
public class WikiController implements Serializable {

    @EJB
    protected WorkflowService workflowService;

    @EJB
    protected DocumentService documentService;

    @Inject
    protected FileUploadController fileUploadController;

    @Inject
    protected WorkflowController workflowController;

    @Inject
    protected BoardController boardController;
    @Inject
    SearchController searchController;

    private static final long serialVersionUID = 1L;
    private int pageSize = 10;
    // private int row = 0;
    private boolean endOfList = false;
    private String dmsFilter = null;
    private String searchFilter;
    private int pageIndex = 0;
    private int pageMax = 0;

    private List<ItemCollection> document = null;
    private List<ItemCollection> documentPage = null;
    private ItemCollection process = null;
    private Map<String, Boolean> selection;
    private Map<String, List<SelectItem>> selectionMainChapters = null;;

    private static Logger logger = Logger.getLogger("ch.foreigntrade.aeo");

    /**
     * Initialize default behavior initialize the processref, page index and phrase
     * 
     */
    @PostConstruct
    public void init() {
        // extract the processref and page from the query string
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = fc.getExternalContext().getRequestParameterMap();

        boardController.setProcessRef(paramMap.get("processref"));
        process = documentService.load(boardController.getProcessRef());

        String _page = paramMap.get("page");
        if (_page != null && !_page.isEmpty()) {
            setPageIndex(Integer.parseInt(_page));
        }
        // reset document ....
        // reset();
    }

    /**
     * Returns true if the current question has an open callback
     * 
     * @return
     */
    public boolean hasOpenCallback(String uid) {

        String searchTerm = "(";
        searchTerm += " type:\"workitem\" AND ";
        searchTerm += " $uniqueidref:\"" + uid + "\" AND $taskid:[3000 TO 3399])";
        try {
            List<ItemCollection> result = documentService.findStubs(searchTerm, 1, 0, null, false);
            if (result.size() > 0) {
                return true;
            }
        } catch (QueryException e) {
            logger.severe("getWorkListByRef - invalid param: " + e.getMessage());
            return false;
        }
        return false;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * This method resets the current document selection. The process is loaded by
     * the given processref.
     * 
     * @param aprocessRef
     */
    public void reset() {
        document = null;
        documentPage = null;
        pageIndex = 0;
        searchFilter = "";

    }

    public String getSearchFilter() {
        if (searchFilter == null) {
            searchFilter = "";
        }
        return searchFilter;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    /**
     * starts a document search.....
     * 
     * @param event
     */
    public void doSearch(ActionEvent event) {
        document = null;
    }

    /**
     * Returns the current process document for the document functions
     * 
     * @return
     */
    public ItemCollection getProcess() {
        return process;
    }

    /**
     * This method returns the complete question document with all published
     * questions. The list contains only simple ItemCollection elements with the
     * minimum of content to avoid waist of memory...
     * <p>
     * The complete list must be loaded because of a paging functionality containing
     * the main chapters
     * 
     * 
     * @return
     */
    public List<ItemCollection> getDocument() {
        if (document == null) {

            document = new ArrayList<ItemCollection>();
            List<ItemCollection> col = null;
            try {
                // select all objects....
                col = documentService.find("(type:\"workitem\") AND ($uniqueidref:\"" + process.getUniqueID() + "\")",
                        999, 0);

                for (ItemCollection aworkitem : col) {
                    document.add(aworkitem);
                }

              
            } catch (QueryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // sort document by chapter
            Collections.sort(document,
                    new ChapterComparator(FacesContext.getCurrentInstance().getViewRoot().getLocale(), true));
        }

       

        return document;
    }

    public List<ItemCollection> getDocumentPage() throws QueryException {
        // now return only the current page segment
        if (documentPage == null) {

            documentPage = new ArrayList<ItemCollection>();
            endOfList = false;
            int iPos = pageIndex * pageSize;

            for (int i = 0; i < pageSize; i++) {
                if (iPos >= getDocument().size()) {
                    endOfList = true;
                    break;
                }
                ItemCollection chapter = document.get(iPos);
                if (chapter != null) {
                    documentPage.add(chapter);
                }
                iPos++;
            }
        }
        return documentPage;

    }

    /**
     * This method returns a SelectItem list with the main chapter numbers.
     * 
     * @return
     */
    public List<SelectItem> getMainChapters(String processref) {
        if (selectionMainChapters == null) {
            selectionMainChapters = new HashMap<String, List<SelectItem>>();
        }

        List<SelectItem> result = selectionMainChapters.get(processref);
        if (result == null) {
            result = new ArrayList<>();
            // create simple itemCollections for the main chapters....
            List<ItemCollection> chapters;
            try {
                // all chapters, ignore current
                String query = "(type:\"workitem\") AND ($workflowgroup:\"Kapitel\" OR $workflowgroup:\"Chapter\") AND ($uniqueidref:\""
                        + processref + "\")";

                if (!workflowController.getWorkitem().getUniqueID().isEmpty()) {
                    query = query + " AND NOT ($uniqueid:" + workflowController.getWorkitem().getUniqueID() + ")";
                }
                chapters = documentService.find(query, 999, 0);

                // sort by number
                Collections.sort(chapters, new ItemCollectionComparator("chapter.number", true));

                for (ItemCollection chaper : chapters) {

                    String label = chaper.getItemValueString("chapter.number") + " "
                            + chaper.getItemValueString("chapter.name");

                    SelectItem s = new SelectItem(chaper.getItemValueString("chapter.number"), label);
                    // s.setDescription(chaper.getItemValueString("_description"));
                    result.add(s);

                }
            } catch (QueryException e) {
                logger.severe("getMainChapters - Unable to load chapters: " + e.getMessage());
            }

            selectionMainChapters.put(processref, result);

        }
        return result;
    }

    /**
     * This method returns a SelectItem list with the main chapter numbers.
     * 
     * @return
     */
    public List<ItemCollection> getMainChaptersByQuestion() {

        List<ItemCollection> list = new ArrayList<ItemCollection>();
        ItemCollection workitem = this.workflowController.getWorkitem();
        String sMainChaper = workitem.getItemValueString("txtMainchapter") + ".";

        if (!"".equals(sMainChaper)) {
            // select main chapters....
            List<ItemCollection> chapters;
            try {
                chapters = documentService.find(
                        "(type:\"workitem\") AND ($workflowgroup:\"Kapitel\" OR $workflowgroup:\"Chapter\") AND ($uniqueidref:\""
                                + workflowController.getWorkitem().getItemValueString("process.ref") + "\")",
                        999, 0);

                // now we take every main chapter which matches the txtName property
                for (ItemCollection chaper : chapters) {
                    String sChapter = chaper.getItemValueString("chapter.number");

                    if (sMainChaper.startsWith(sChapter)) {

                        ItemCollection itemCol = new ItemCollection();
                        itemCol.replaceItemValue("chapter.Name", sChapter);
                        itemCol.replaceItemValue("chapter.name", chaper.getItemValueString("chapter.name"));
                        list.add(itemCol);

                    }

                }
            } catch (QueryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return list;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getSelectedQuestions() {
        if (selection == null)
            selection = new HashMap();
        return selection;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * This method increases the search PageIndex and updates the bookmarkable
     * search link to the worklist including the current search phrase.
     * 
     */
    public String getNext() {
        documentPage = null;
        this.setPageIndex(this.getPageIndex() + 1);
        return getBookmark();
    }

    /**
     * This method increases the search PageIndex and updates the bookmarkable
     * search link to the worklist including the current search phrase.
     * 
     */
    public String getPrev() {
        documentPage = null;
        this.setPageIndex(this.getPageIndex() - 1);
        return getBookmark();
    }

    public String getPage(int index) {
        documentPage = null;
        this.setPageIndex(index);
        return getBookmark();
    }

    private String getBookmark() {
        String phrase = searchController.getSearchFilter().getItemValueString("phrase");

        try {
            phrase = URLEncoder.encode(phrase, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.severe("unable to encode search phrase!");
            e.printStackTrace();
        }
        String action = "/pages/workitems/forms/wiki/document.xhtml?faces-redirect=true&page=" + getPageIndex() + "&processref="
                + boardController.getProcessRef() + "&phrase=" + phrase;

        return action;
    }

    /**
     * returns the number of pages
     * 
     * @return
     * @throws QueryException
     */
    public int getPageMax() throws QueryException {

        if (document == null) {
            // initialize document
            getDocument();
        }

        if (document != null) {
            double pages = 1;
            double documentSize = document.size();
            if (documentSize > 0) {
                pages = Math.ceil(documentSize / pageSize);
            }
            pageMax = ((int) pages);
        } else {
            pageMax = 0;
        }

        return pageMax;
    }
//
//	public int getRow() {
//		return row;
//	}
//
//	public void setRow(int row) {
//		this.row = row;
//	}

    public boolean isEndOfList() {
        return endOfList;
    }

//    public int getPageMax() {
//        return pageMax;
//    }

    public void setEndOfList(boolean endOfList) {
        this.endOfList = endOfList;
    }

    /**
     * Set default value for AEO Question. keyReminder will be set to '0'
     * datReminder will be set to + one year
     * 
     * @param workflowEvent
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void onWorkflowEvent(@Observes WorkflowEvent workflowEvent) {
        if (workflowEvent == null)
            return;

        ItemCollection workitem = workflowEvent.getWorkitem();

        String model = workitem.getModelVersion();
        if (!model.startsWith("frage-") && !model.startsWith("question-")) {
            return;
        }

        if (WorkflowEvent.WORKITEM_CREATED == workflowEvent.getEventType()) {
            // keyReminder and datReminder
            workitem.replaceItemValue("keyReminder", "-1");
            Calendar cal = Calendar.getInstance();

            cal.add(Calendar.YEAR, 1);
            workitem.replaceItemValue("datReminder", cal.getTime());
            workitem.replaceItemValue("_attachements", "0");

            selection = new HashMap();

        }

        if (WorkflowEvent.WORKITEM_CHANGED == workflowEvent.getEventType()) {
            // update _relevant based on question selection
            selection = new HashMap();
            if (workitem != null) {
                List<String> vSelection = workitem.getItemValue("_relevant");
                for (String akey : vSelection) {
                    selection.put(akey, true);
                }
            }
            // reset dms filter
            setDmsFilter(null);

            // set process...
            process = documentService.load(workitem.getItemValueString("txtprocessref"));
        }

        if (WorkflowEvent.WORKITEM_BEFORE_PROCESS == workflowEvent.getEventType()) {
            document = null;

            // Migrations Helper Class (wegen alt daten)
            if (workitem.getItemValueString("process.ref").isEmpty()) {
                workitem.setItemValue("process.ref", workitem.getItemValue("txtprocessref"));
            }
            if (workitem.getItemValueString("space.ref").isEmpty()) {
                workitem.setItemValue("space.ref", workitem.getItemValue("txtspaceref"));
            }

            /*
             * List<String> files = fileUploadController.getAttachedFiles();
             * 
             * // jetzt speichern wird die Liste der Dateien im Feld "$Filenames", // damit
             * wir // die Information im Report nutzen können Vector v = new
             * Vector(Arrays.asList(files)); workitem.replaceItemValue("$FileNames", v);
             * String aeoName = "aeo-" + workitem.getItemValueString("txtname") + ".pdf";
             * v.remove(aeoName); workitem.replaceItemValue("_aeoFileNames", v);
             * 
             */

            // update _relevant based on question selection
            if (selection != null) {
                Vector<String> newSelection = new Vector<String>();
                for (Map.Entry<String, Boolean> entry : selection.entrySet()) {
                    if (entry.getValue() == true)
                        newSelection.add(entry.getKey());
                }
                workitem.replaceItemValue("_relevant", newSelection);
                logger.info("Question selected=" + newSelection);
            }

        }

        if (WorkflowEvent.WORKITEM_AFTER_PROCESS == workflowEvent.getEventType()) {
            this.reset();
        }
    }

    /**
     * Filter box for filtering DMS List
     * 
     * @return
     */
    public String getDmsFilter() {
        return dmsFilter;
    }

    public void setDmsFilter(String dmsFitler) {
        this.dmsFilter = dmsFitler;
    }

    /**
     * returns a List with all Versions of the current Workitem
     * 
     * removes all deleted versions!
     * 
     * @return
     */
//	public List<ItemCollection> getVersions() {
//		List<ItemCollection> versions = workflowController.getVersions();
//
//		// remove all deleted versions!
//		List<ItemCollection> result = new ArrayList<ItemCollection>();
//		for (ItemCollection aversion : versions) {
//			if (!"workitemversiondeleted".equals(aversion.getItemValueString("type"))) {
//				result.add(aversion);
//			}
//		}
//
//		return result;
//	}

}
