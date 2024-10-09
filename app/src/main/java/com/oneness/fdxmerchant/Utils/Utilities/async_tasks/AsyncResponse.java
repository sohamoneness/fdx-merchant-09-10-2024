/*
 * Copyright (c) 2016.
 * Soham Ghosh
 */

package com.oneness.fdxmerchant.Utils.Utilities.async_tasks;

public interface AsyncResponse
{
    /**
     * This method return the result from the web service response.
     */
    void processFinish(String type, String output);
}
