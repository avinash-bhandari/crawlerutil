package com.emailcrawler.crawlerutil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

import com.emailcrawler.crawlerutil.EmailCrawlerTest;

/**
 * Unit test for simple App.
 */
public class EmailCrawlerTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public EmailCrawlerTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(EmailCrawlerTest.class);
	}

	/**
	 * Sample Test
	 */
	public void testApp() {
		assertTrue(true);
//		EmailCrawler crawler = new EmailCrawler("web.mit.edu");
//		List<String> emails = crawler.crawlforEmails();
//		assertEquals(emails.size(), 33);
	}
}
