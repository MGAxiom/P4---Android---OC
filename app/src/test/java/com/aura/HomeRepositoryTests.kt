package com.aura

import com.aura.data.remote.AccountResponse
import com.aura.data.remote.ApiService
import com.aura.data.repositories.HomeRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class HomeRepositoryTest {

  @Mock
  private lateinit var mockApiService: ApiService

  private lateinit var homeRepository: HomeRepository

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)
    homeRepository = HomeRepository(mockApiService)
  }

  @Test
  fun `getBalance returns correct balance for main account`() = runBlocking {
    // Given
    val id = "user123"
    val mainAccount = AccountResponse(id = "account1", isMain = true, balance = 100.0)
    val secondaryAccount = AccountResponse(id = "account2", isMain = false, balance = 50.0)
    whenever(mockApiService.getAccount(id)).thenReturn(listOf(mainAccount, secondaryAccount))

    // When
    val result = homeRepository.getBalance(id)

    // Then
    assertEquals(100.0, result, 0.000001)
  }

  @Test
  fun `getBalance returns 0 when no main account found`() = runBlocking {
    // Given
    val id = "user123"
    val account1 = AccountResponse(id = "account1", isMain = false, balance = 100.0)
    val account2 = AccountResponse(id = "account2", isMain = false, balance = 50.0)
    whenever(mockApiService.getAccount(id)).thenReturn(listOf(account1, account2))

    // When
    val result = homeRepository.getBalance(id)

    // Then
    assertEquals(0.0, result, 0.000001)
  }

  @Test
  fun `getBalance returns 0 when API call throws exception`() = runBlocking {
    // Given
    val id = "user123"
    whenever(mockApiService.getAccount(id)).thenThrow(RuntimeException("API error"))

    // When
    val result = homeRepository.getBalance(id)

    // Then
    assertEquals(0.0, result, 0.000001)
  }

  @Test
  fun `getBalance returns 0 when account list is empty`() = runBlocking {
    // Given
    val id = "user123"
    whenever(mockApiService.getAccount(id)).thenReturn(emptyList())

    // When
    val result = homeRepository.getBalance(id)

    // Then
    assertEquals(0.0, result, 0.000001)
  }
}