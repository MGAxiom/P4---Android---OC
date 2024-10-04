package com.aura

import com.aura.data.remote.ApiService
import com.aura.data.remote.LoginRequest
import com.aura.data.remote.LoginResponse
import com.aura.data.repositories.LoginRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class LoginRepositoryTest {

    @Mock
    private lateinit var mockApiService: ApiService

    private lateinit var loginRepository: LoginRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loginRepository = LoginRepository(mockApiService)
    }

    @Test
    fun `login returns true when API grants access`() = runBlocking {
        // Given
        val id = "user"
        val password = "password"
        whenever(mockApiService.login(any())).thenReturn(LoginResponse(granted = true))

        // When
        val result = loginRepository.login(id, password)

        // Then
        assertTrue(result)
    }

    @Test
    fun `login returns false when API denies access`() = runBlocking {
        // Given
        val id = "user"
        val password = "wrongPassword"
        whenever(mockApiService.login(any())).thenReturn(LoginResponse(granted = false))

        // When
        val result = loginRepository.login(id, password)

        // Then
        assertFalse(result)
    }

    @Test
    fun `login returns false when API call throws exception`() = runBlocking {
        // Given
        val id = "user"
        val password = "password"
        whenever(mockApiService.login(any())).thenThrow(RuntimeException("Network error"))

        // When
        val result = loginRepository.login(id, password)

        // Then
        assertFalse(result)
    }

    @Test
    fun `login passes correct LoginRequest to API`() = runBlocking {
        // Given
        val id = "user"
        val password = "password"
        whenever(mockApiService.login(LoginRequest(id, password))).thenReturn(LoginResponse(granted = true))

        // When
        val result = loginRepository.login(id, password)

        // Then
        assertTrue(result)
    }
}