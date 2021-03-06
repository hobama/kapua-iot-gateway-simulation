/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

package storage;


import com.google.gson.Gson;

import java.util.UUID;

/**
 * Defines an Item. A item is stored in an ItemStore. It can be for example a Product, a Order, etc.
 * @author Arthur Deschamps
 * @since 1.0
 * @see ItemStore
 */
public abstract class Item {

    private String id = UUID.randomUUID().toString();

    /**
     * Validate that object conforms to the schema
     * @return
     * True if the object is validated. False otherwise.
     */
    public abstract boolean validate();

    /**
     * Converts the item to a Json object.
     * @return
     * The item converted into Json format.
     */
    public String toJson() {
        return new Gson().toJson(this);
    }

    /**
     * Returns an id unique to each item.
     * @return
     * The id.
     */
    public String getId() {
        return id;
    }
}
