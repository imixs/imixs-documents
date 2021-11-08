package org.imixs.workflow.office.wiki;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.StringTokenizer;

import org.imixs.workflow.ItemCollection;

public class ChapterComparator implements Comparator<ItemCollection> {
		private final Collator collator;

		private final boolean ascending; 

		public ChapterComparator(boolean ascending) {
			this.collator = Collator.getInstance();
			this.ascending = ascending;
		}
		
		public ChapterComparator(Locale locale, boolean ascending) {
			this.collator = Collator.getInstance(locale);
			this.ascending = ascending;
		}

		public int compare(ItemCollection a, ItemCollection b) {
			int result = this.collator.compare(
					convertChapter(a.getItemValueString("txtName")),
					convertChapter(b.getItemValueString("txtname")));

			if (!this.ascending) {
				result = -result;
			}
			return result;
		}

		/**
		 * add leading zeros
		 * 
		 * @param achapter
		 * @return
		 */
		private String convertChapter(String achapter) {
			String sChapterNew = "";
			StringTokenizer st = new StringTokenizer(achapter, ".");
			while (st.hasMoreElements()) {
				String part = "00000000" + st.nextToken();
				sChapterNew += part.substring(part.length() - 7);
			}
			return sChapterNew;

		}

	}



