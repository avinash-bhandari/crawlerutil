package com.emailcrawler.crawlerutil;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Helper to scan all email address found on a website. Crawls all pages of the
 * specific domain looking for email addresses. Uses the jsoup parser to parse
 * pages for links and emails.
 */
public class DomainCrawler {
	// Do not god beyond MAX_DEPTH when following links
	private static final int MAX_DEPTH = 25;
	// Base URL to start from
	private String rootUrl;
	// Domain name the base url is mapped to
	private String domainName;
	// Placeholder for links scanned
	private HashSet<String> links;
	// Placeholder for emails found
	private HashMap<String,String> emails;
	// The selector to match links to the specific domain
	private String linkSelector;

	/**
	 * DomainCrawler Crawl a domain recursively for email addresses.
	 * 
	 * @param url
	 */
	public DomainCrawler(String url) {
		rootUrl = getAbsoluteUrl(url);
		init();
	}

	private void init() {

		links = new HashSet<String>();
		emails = new HashMap<String,String>();
		this.domainName = getDomainName(rootUrl);
		linkSelector = "a[href*=" + domainName + "]";
	}

	/**
	 * Fetch all the emails of the domain
	 * 
	 * @return
	 */
	public List<String> getEmails() {

		init();

		if (StringUtil.isBlank(domainName)) {
			System.err.println("Invalid domain name for url " + rootUrl);
		} else {
			getEmailsFromPage(rootUrl, 0);
		}
		System.out.println("===========>");
		List<String> emailList = new ArrayList<String>(emails.keySet());
		for (String email : emailList) {
			System.out.println(email);
		}
		System.out.println("===========> Scanned " + links.size() + " pages.  Found " + emailList.size() + " unique emails ");

		return emailList;
	}

	/**
	 * Recursively crawl for pages until a specified depth is reached.
	 * 
	 * @param currentURL
	 * @param depth
	 */
	private void getEmailsFromPage(String currentURL, int depth) {

		String domain = getDomainName(currentURL);

		if ((domainName.equals(domain)) && (!links.contains(currentURL)) && (depth < MAX_DEPTH)) {
			System.out.println(">> Depth: " + depth + " [" + currentURL + "]");
			try {
				depth++;

				links.add(currentURL);

				Document document = Jsoup.connect(currentURL).get();

				Elements emailListOnPage = document.select("a[href*=mailto]");
				Elements linkListOnPage = document.select(linkSelector);

				for (Element page : linkListOnPage) {
					String newUrl = getAbsoluteUrl(page.attr("abs:href"));
					getEmailsFromPage(newUrl, depth);
				}

				for (Element page : emailListOnPage) {
					//System.out.println("Email " + page.attr("abs:mailto") + " , text " + page.text());
					String email = page.text();
					if (isValidEmailAddress(email)) {
						emails.put(email,email);
					}
				}

			} catch (IOException e) {
				// ignore
				//System.err.println("Exception Reading URL " + currentURL + "': " + e.getMessage());
			}
		}
	}
	
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
			  // System.err.println("Invalid email : " + email);
		      result = false;
		   }
		   return result;
		}

	private String getAbsoluteUrl(String url) {
		String newUrl = url;

		if (!StringUtil.isBlank(newUrl) && !newUrl.startsWith("http") && !newUrl.startsWith("https")) {
			newUrl = "http://" + newUrl;
		}

		return newUrl;
	}


	private String getDomainName(String url) {
		URI uri;
		String hostname = null;
		try {
			uri = new URI(url);
			hostname = uri.getHost();
			if ((!StringUtil.isBlank(hostname)) && hostname.startsWith("www")) {
				hostname = hostname.substring(4);
			}

		} catch (URISyntaxException e) {
			System.err.println("Exception : " + e.getMessage());
		}

		return hostname;
	}

}