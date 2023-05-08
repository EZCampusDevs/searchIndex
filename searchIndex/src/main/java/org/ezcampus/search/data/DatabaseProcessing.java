package org.ezcampus.search.data;

import org.ezcampus.search.hibernate.entity.ScrapeHistory;
import org.ezcampus.search.hibernate.entity.ScrapeHistoryDAO;
import org.tinylog.Logger;

public class DatabaseProcessing
{

	public static void processLastScrape()
	{
		ScrapeHistory lastScrape = ScrapeHistoryDAO.getNewestScrapeHistory();
		
		if (lastScrape == null) {
			Logger.warn("Cannot index data because there is no known scrape history!");
			return ;
		}
		
		Logger.info("Last found scrape time was: {}", lastScrape.toString());
		
		if(lastScrape.getHasBeenIndexed())
		{
			Logger.info("No data to process, last scrape time was already indexed.");
			return ;
		}
		
		ScrapeHistoryDAO.setIndexById(lastScrape.getScrapeId(), true);
		return ;
	}
}
