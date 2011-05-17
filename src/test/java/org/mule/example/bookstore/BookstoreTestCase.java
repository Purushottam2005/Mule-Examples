/*
 * $Id: BookstoreTestCase.java 19191 2010-08-25 21:05:23Z tcarlson $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.example.bookstore;

import org.mule.tck.FunctionalTestCase;

import java.util.Collection;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class BookstoreTestCase extends FunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "bookstore-config.xml";
    }
    
    public void testGetBooks()
    {
        // Catalog web service
        JaxWsProxyFactoryBean pf = new JaxWsProxyFactoryBean();
        pf.setServiceClass(CatalogService.class);
        pf.setAddress(CatalogService.URL);
        CatalogService catalog = (CatalogService) pf.create();
        assertNotNull(catalog);

        Collection <Book> books = catalog.getBooks();
        assertNotNull(books);
        // Number of books added as test data in CatalogServiceImpl.initialise()
        assertEquals(13, books.size());
    }

    public void testOrderBook()
    {
        // Catalog web service
        JaxWsProxyFactoryBean pf = new JaxWsProxyFactoryBean();
        pf.setServiceClass(CatalogService.class);
        pf.setAddress(CatalogService.URL);
        CatalogService catalog = (CatalogService) pf.create();
        assertNotNull(catalog);

        // Order web service
        JaxWsProxyFactoryBean pf2 = new JaxWsProxyFactoryBean();
        pf2.setServiceClass(OrderService.class);
        pf2.setAddress(OrderService.URL);
        OrderService orderService = (OrderService) pf2.create();     
        assertNotNull(orderService);

        // Place an order for book #3 from the catalog
        Book book = catalog.getBook(3); 
        assertNotNull(book);
        Order order = orderService.orderBook(book, 2, "Somewhere", "me@my-mail.com"); 
        assertNotNull(order);
        assertEquals(3, order.getBook().getId());
        assertEquals(2, order.getQuantity());
        assertEquals("me@my-mail.com", order.getEmail());
    }

//    public void testAddBook() throws Exception
//    {
//        HttpServletRequest request = new Request();
//        request.setAttribute("title", "blah");
//        request.setAttribute("author", "blah");
//        
//        MuleClient client = new MuleClient(muleContext);
//        client.send("servlet://catalog", request, null);
//    }
}
