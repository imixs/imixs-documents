package org.imixs.workflow.office.wiki;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.event.Observes;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.ItemCollectionComparator;
import org.imixs.workflow.WorkflowKernel;
import org.imixs.workflow.engine.DocumentService;
import org.imixs.workflow.engine.WorkflowService;
import org.imixs.workflow.exceptions.QueryException;
import org.imixs.workflow.faces.data.WorkflowController;
import org.imixs.workflow.faces.data.WorkflowEvent;
import org.imixs.workflow.faces.fileupload.FileUploadController;
import org.imixs.workflow.office.forms.WorkitemLinkController;
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
     * This method returns all chepters with $taskid 1000-1999. 
     * <p>
     * 
     * 
     * 
     * @return
     */
    public List<ItemCollection> getDocument() {
        if (document == null) {

            document = new ArrayList<ItemCollection>();
            List<ItemCollection> col = null;
            try {
                String query = "(type:\"workitem\") AND ($taskid:[1000 TO 1999]) AND ($uniqueidref:\"" + process.getUniqueID() + "\")";
                // select all objects....
                document = documentService.find(query, 999, 0, "name", false);
//
//                for (ItemCollection aworkitem : col) {
//                    document.add(aworkitem);
//                }

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
                String query = "(type:\"workitem\") AND ($taskid:[1000 TO 1999]) AND ($uniqueidref:\"" + processref + "\")";

                if (!workflowController.getWorkitem().getUniqueID().isEmpty()) {
                    query = query + " AND NOT ($uniqueid:" + workflowController.getWorkitem().getUniqueID() + ")";
                }

                // select all objects....
                chapters = documentService.find(query, 999, 0, "name", false);

                // build a list of JSF SelectItems
                for (ItemCollection chaper : chapters) {
                    String label = chaper.getItemValueString("$workflowsummary");
                    SelectItem s = new SelectItem(chaper.getItemValueString("name"), label);
                    result.add(s);
                }
            } catch (QueryException e) {
                logger.severe("getMainChapters - Unable to load chapters: " + e.getMessage());
            }

            selectionMainChapters.put(processref, result);

        }
        return result;
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
        String action = "/pages/workitems/forms/wiki/document.xhtml?faces-redirect=true&page=" + getPageIndex()
                + "&processref=" + boardController.getProcessRef() + "&phrase=" + phrase;

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

    public boolean isEndOfList() {
        return endOfList;
    }

    public void setEndOfList(boolean endOfList) {
        this.endOfList = endOfList;
    }

    /**
     * The WikiController reacts on the event 'WORKITEM_BEFORE_PROCESS' and resets
     * the wiki document cache.
     * 
     * @param workflowEvent
     */
    public void onWorkflowEvent(@Observes WorkflowEvent workflowEvent) {
        if (workflowEvent == null)
            return;

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
     * returns a list of all workItems holding a reference to a given workItem.
     * 
     * 
     * @return
     * @param filter
     * @throws NamingException
     */
    public List<ItemCollection> getReferences(String uniqueid) {
        List<ItemCollection> result = new ArrayList<ItemCollection>();

        // select all references.....
        String sQuery = "(";
        sQuery = " (type:\"workitem\") AND ($uniqueidref:\"" + uniqueid + "\")";

        List<ItemCollection> workitems = null;

        try {
            workitems = workflowService.getDocumentService().findStubs(sQuery, WorkitemLinkController.MAX_SEARCH_RESULT, 0,
                    WorkflowKernel.LASTEVENTDATE, true);
        } catch (QueryException e) {

            e.printStackTrace();
        }
        // sort by modified
        Collections.sort(workitems, new ItemCollectionComparator("$created", true));

        result.addAll(workitems);
        return result;

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
