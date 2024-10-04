package com.aura

import com.aura.data.remote.ApiService
import com.aura.data.remote.TransferRequest
import com.aura.data.remote.TransferResponse
import com.aura.data.repositories.TransferRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class TransferRepositoryTest {

    @Mock
    private lateinit var mockApiService: ApiService

    private lateinit var transferRepository: TransferRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        transferRepository = TransferRepository(mockApiService)
    }

    @Test
    fun `transferFunds returns true when transfer is successful`() = runBlocking {
        // Given
        val sender = "sender"
        val recipient = "recipient"
        val amount = 100.0
        whenever(mockApiService.transfer(any())).thenReturn(TransferResponse(result = true))

        // When
        val result = transferRepository.transferFunds(sender, recipient, amount)

        // Then
        assertTrue(result)
    }

    @Test
    fun `transferFunds returns false when transfer fails`() = runBlocking {
        // Given
        val sender = "sender"
        val recipient = "recipient"
        val amount = 100.0
        whenever(mockApiService.transfer(any())).thenReturn(TransferResponse(result = false))

        // When
        val result = transferRepository.transferFunds(sender, recipient, amount)

        // Then
        assertFalse(result)
    }

    @Test
    fun `transferFunds passes correct TransferRequest to API`() = runBlocking {
        // Given
        val sender = "sender"
        val recipient = "recipient"
        val amount = 100.0
        val expectedRequest = TransferRequest(sender, recipient, amount)
        whenever(mockApiService.transfer(expectedRequest)).thenReturn(TransferResponse(result = true))

        // When
        val result = transferRepository.transferFunds(sender, recipient, amount)

        // Then
        assertTrue(result)
    }
}