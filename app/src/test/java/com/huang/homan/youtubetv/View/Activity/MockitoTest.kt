package com.huang.homan.youtubetv.View.Activity

import android.util.Log
import com.huang.homan.youtubetv.Model.PriceTag
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks









class MockitoTest {
    private val TAG = "MYLOG MockitoTest"
    private fun ltag(message: String) { Log.i(TAG, message) }

    private lateinit var mainActivity: MainActivity
    private val httpResponseDelay : Long = 5000

    @Before
    fun init() {
        // Enable Mockito Annotations
        MockitoAnnotations.initMocks(this)
    }

    @Mock
    private var mockListVar: ArrayList<String>? = null

    @Test  // #1
    fun whenNotUseMockAnnotation_thenCorrect_var() {
        mockListVar?.add("Huang")
        verify(mockListVar)?.add("Huang")
        assertEquals(0, mockListVar?.size)

        mockListVar?.add("Homan")
        verify(mockListVar)?.add("Homan")
        assertEquals(0, mockListVar?.size)

        `when`(mockListVar?.size).thenReturn(100)
        assertEquals(100, mockListVar?.size)
    }

    @Mock
    private val mockListVal: ArrayList<String>? = null

    @Test // #2
    fun MockAnnotation_thenCorrect_val() {
        mockListVal!!.add("Huang")
        verify(mockListVal).add("Huang")
        assertEquals(0, mockListVal.size)

        mockListVal.add("Homan")
        verify(mockListVal).add("Homan")
        assertEquals(0, mockListVal.size)

        `when`(mockListVal.size).thenReturn(100)
        assertEquals(100, mockListVal.size)
    }

    @Test
    fun SpyAnnotation_Test() {
        val spiedList = spy<ArrayList<String>>()

        spiedList!!.add("My")
        spiedList.add("name")

        verify(spiedList).add("My")
        verify(spiedList).add("name")
        // Make sure with 2 items
        assertEquals(2, spiedList.size)
        // Stub answer with 100 if existed size
        doReturn(100).`when`(spiedList).size
        assertEquals(100, spiedList.size)

        doReturn(true).`when`(spiedList).contains("Not existed")
        assertTrue(spiedList.contains("Not existed"))
    }

    @Test
    fun Inline_Mock_Test() {
        val mockList = mock<ArrayList<String>>()

        mockList.add("Homan Huang")
        verify(mockList).add("Homan Huang")
        assertEquals(0, mockList.size)

        `when`(mockList.size).thenReturn(100)
        assertEquals(100, mockList.size)
    }

    @Test
    fun CaptorAnnotation_thenCorrect() {
        val mockList = mock<ArrayList<String>>()
        val arg = ArgumentCaptor.forClass(String::class.java)

        mockList.add("one")
        verify(mockList).add(arg.capture())

        assertEquals("one", arg.value)
    }

    @Mock
    val mockedcapList: ArrayList<String>? = null

    @Captor
    var argCaptor: ArgumentCaptor<String>? = null

    @Test
    fun whenUseCaptorAnnotation_thenTheSam() {
        mockedcapList!!.add("one")
        verify(mockedcapList).add(argCaptor!!.capture())

        assertEquals("one", argCaptor!!.value)
    }


    @Mock
    val mPriceTag: MutableMap<String, Float>? = null

    @Spy
    @InjectMocks
    var priceDB = PriceTag()

    @Test
    fun InjectMocksAnnotation_Test() {
        // add data to DB
        //priceDB.add("Apple", 2.0f)

        // mock data: you don't need to put and get
        `when`(mPriceTag!!.get("Apple")).thenReturn(2.0f)

        // inject mock method
        `when`(priceDB!!.getPrice("Apple")).thenReturn(2.0f)

        assertEquals(mPriceTag.get("Apple"), priceDB.getPrice("Apple"))
    }

}

