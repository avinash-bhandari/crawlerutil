package com.emailcrawler.crawlerutil;

import java.net.URISyntaxException;
import java.util.List;

/**
 * EmailCrawer 
 * Crawl a specific domain provided by the user and find all email addresses.
 * 
 **/
public class EmailCrawler {

	// The Web page provided by the user
	String webpage;

	public EmailCrawler(String webpage) {
		this.webpage = webpage;
	}

	public List<String> crawlforEmails() {
		DomainCrawler crawler = new DomainCrawler(webpage);
		List<String> emails = crawler.getEmails();
		System.out.println("EmailCrawler completed found " + emails.size() + " email addresses.");
		return emails;
	}

	public static void main(String[] args) throws URISyntaxException {

		if (args.length != 1) {
			System.out.println("Java -jar <jarfile> <domain name>");
			System.out.println("java -jar target/crawlerutil-jar-with-dependencies.jar web.mit.edu");
			return;
		}
		String url = args[0];

		System.out.println("EmailCrawler start");

		EmailCrawler crawler = new EmailCrawler(url);

		crawler.crawlforEmails();
	}
}