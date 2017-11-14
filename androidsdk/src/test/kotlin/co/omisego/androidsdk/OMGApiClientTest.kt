package co.omisego.androidsdk

import co.omisego.androidsdk.models.*
import co.omisego.androidsdk.utils.APIErrorCode
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.amshove.kluent.shouldNotBeInstanceOf
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.test.assertTrue

/**
 * OmiseGO
 *
 *
 * Created by Phuchit Sirimongkolsathien on 11/10/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class OMGApiClientTest {
    private val TEST_AUTHORIZATION_TOKEN = "OMGClient MTQ4MnFOeFBleTdBNF9ycktrQU9iNGtBT1RzRDJIb0x5c1M3ZVExWmQzWTo5WFdoSWR0a0FOTUZ4RGhBRlRVUUJTaFItakdvR2V5b0MyRjQ0ZFpmcGlJ"

    @Before
    fun setUp() {
        OMGApiClient.init(TEST_AUTHORIZATION_TOKEN, EmptyCoroutineContext)
    }

    @Test
    fun `get user success`() = runBlocking {
        // Arrange
        var actualResponse: Response<Any>? = null

        // Just don't care about thread here. Because in android, will work properly.
        // Action
        OMGApiClient.getCurrentUser(object : Callback<User> {
            override fun success(response: Response<User>) {
                actualResponse = response
            }

            override fun fail(response: Response<ApiError>) {
                actualResponse = response
            }
        })

        delay(3000)

        // Assert
        actualResponse shouldNotBe null
        val model = actualResponse!!.data
        model shouldNotBeInstanceOf ApiError::class

        val user = model as User
        user.id shouldNotBe null
        user.metaData shouldNotBe null
        user.providerUserId shouldNotBe null
        user.username shouldNotBe null
    }

    @Test
    fun `get user failed because invalid auth scheme`() = runBlocking {
        // Arrange
        var actualResponse: Response<Any>? = null
        OMGApiClient.init("wrong token", EmptyCoroutineContext)

        // Just don't care about thread here. Because in android, will work properly.
        // Action
        OMGApiClient.getCurrentUser(object : Callback<User> {
            override fun success(response: Response<User>) {
                actualResponse = response
            }

            override fun fail(response: Response<ApiError>) {
                actualResponse = response
            }
        })

        delay(3000)

        // Assert
        actualResponse shouldNotBe null
        val model = actualResponse!!.data
        model shouldNotBeInstanceOf User::class

        val user = model as ApiError
        user.code shouldEqual APIErrorCode.CLIENT_INVALID_AUTH_SCHEME
    }

    @Test
    fun logout() {

    }

    @Test
    fun `list balances success`() = runBlocking {
        // Arrange
        var actualResponse: Response<Any>? = null

        // Just don't care about thread here. Because in android, will work properly.
        // Action
        OMGApiClient.listBalances(object : Callback<List<Address>> {
            override fun success(response: Response<List<Address>>) {
                actualResponse = response
            }

            override fun fail(response: Response<ApiError>) {
                actualResponse = response
            }
        })

        delay(3000)

        // Assert
        actualResponse shouldNotBe null

        val model = actualResponse!!.data
        assertTrue(model is List<*>)

        val listAddress = model as List<Address>
        listAddress.size shouldEqual 1
        listAddress[0].balances.size shouldEqual 1
        listAddress[0].balances[0].amount shouldEqual 10000.0
        listAddress[0].balances[0].mintedToken.symbol shouldEqual "OMG"
        listAddress[0].balances[0].mintedToken.name shouldEqual "OmiseGO"
        listAddress[0].balances[0].mintedToken.subUnitToUnit shouldEqual 100.0
    }

    @Test
    fun `list balances should fail because wrong token given`() = runBlocking {
        // Arrange
        var actualResponse: Response<Any>? = null
        OMGApiClient.init("wrong token", EmptyCoroutineContext)

        // Just don't care about thread here. Because in android, will work properly.
        // Action
        OMGApiClient.listBalances(object : Callback<List<Address>> {
            override fun success(response: Response<List<Address>>) {
                actualResponse = response
            }

            override fun fail(response: Response<ApiError>) {
                actualResponse = response
            }
        })

        delay(3000)

        // Assert
        actualResponse shouldNotBe null

        println(actualResponse)

        val model = actualResponse!!.data
        assertTrue(model !is List<*>)

        val setting = model as ApiError
        setting.code shouldEqual APIErrorCode.CLIENT_INVALID_AUTH_SCHEME
    }

    @Test
    fun `get settings success`() = runBlocking {
        // Arrange
        var actualResponse: Response<Any>? = null

        // Just don't care about thread here. Because in android, will work properly.
        // Action
        OMGApiClient.getSettings(object : Callback<Setting> {
            override fun success(response: Response<Setting>) {
                actualResponse = response
            }

            override fun fail(response: Response<ApiError>) {
                actualResponse = response
            }
        })

        delay(3000)

        // Assert
        actualResponse shouldNotBe null
        val model = actualResponse!!.data
        model shouldNotBeInstanceOf ApiError::class

        val setting = model as Setting
        setting.mintedTokens.size shouldEqual 2
    }

    @Test
    fun `get settings failed because invalid auth scheme`() = runBlocking {
        // Arrange
        var actualResponse: Response<Any>? = null
        OMGApiClient.init("wrong token", EmptyCoroutineContext)

        // Just don't care about thread here. Because in android, will work properly.
        // Action
        OMGApiClient.getSettings(object : Callback<Setting> {
            override fun success(response: Response<Setting>) {
                actualResponse = response
            }

            override fun fail(response: Response<ApiError>) {
                actualResponse = response
            }
        })

        delay(3000)

        // Assert
        actualResponse shouldNotBe null
        val model = actualResponse!!.data
        model shouldNotBeInstanceOf Setting::class

        val setting = model as ApiError
        setting.code shouldEqual APIErrorCode.CLIENT_INVALID_AUTH_SCHEME
    }

}