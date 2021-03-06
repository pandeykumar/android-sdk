package co.omisego.omisego.custom

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse

/**
 * A callback that represents whether an API request was successful or returned an error.
 *
 */
interface OMGCallback<in T> {
    /**
     * The request and post processing operations were successful resulting in the serialization
     * of the provided associated data
     *
     * @param response The serialization of the provided associated data
     */
    fun success(response: OMGResponse<T>)

    /**
     * The request encountered an error resulting in a failure
     *
     * @param response The serialization of an error which represents in [APIError]
     */
    fun fail(response: OMGResponse<APIError>)
}
