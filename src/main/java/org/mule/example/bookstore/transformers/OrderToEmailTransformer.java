/*
 * $Id: OrderToEmailTransformer.java 19250 2010-08-30 16:53:14Z dirk.olmes $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.example.bookstore.transformers;

import org.mule.RequestContext;
import org.mule.api.transformer.TransformerException;
import org.mule.example.bookstore.Book;
import org.mule.example.bookstore.Order;
import org.mule.transformer.AbstractTransformer;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.transport.email.MailProperties;

/**
 * Composes an e-mail notification message to be sent based on the Book Order.
 */
public class OrderToEmailTransformer extends AbstractTransformer
{
    public OrderToEmailTransformer()
    {
        super();
        registerSourceType(DataTypeFactory.create(Order.class));
        setReturnDataType(DataTypeFactory.STRING);
    }
    
    @Override
    protected Object doTransform(Object src, String outputEncoding) throws TransformerException
    {
        Order order = (Order) src;
        Book book = order.getBook();
        
        String body =  "Thank you for placing your order for " +
                       book.getTitle() + " with the Mule-powered On-line Bookstore. " +
                       "Your order will be shipped  to " +
                       order.getAddress() + " by the next business day.";
        
        String email = order.getEmail();
        RequestContext.getEventContext().getMessage().setOutboundProperty(MailProperties.TO_ADDRESSES_PROPERTY,
                                                                          email);
        logger.info("Sending e-mail notification to " + email);
        return body;
    }
}
