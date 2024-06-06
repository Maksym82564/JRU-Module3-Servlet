package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FieldTest {
    private Field field;
    private Map<Integer, Sign> fieldMap;

    @BeforeEach
    public void setUp() {
        field = new Field();
        fieldMap = field.getField();
    }

    @Test
    public void testFieldConstructor() {
        assertNotNull(fieldMap, "Field map should not be null");
        assertEquals(9, fieldMap.size(), "Field map should contain 9 entries");

        for (int i = 0; i < 9; i++) {
            assertEquals(Sign.EMPTY, fieldMap.get(i), "Field entry " + i + " should be EMPTY");
        }
    }

    @Test
    public void testGetEmptyFieldIndex_AllFieldsEmpty() {
        assertEquals(0, field.getEmptyFieldIndex(), "First empty field should be at index 0");
    }

    @Test
    public void testGetEmptyFieldIndex_SomeFieldsFilled() {
        fieldMap.put(0, Sign.CROSS);
        fieldMap.put(1, Sign.NOUGHT);

        assertEquals(2, field.getEmptyFieldIndex(), "First empty field should be at index 2");
    }

    @Test
    public void testGetEmptyFieldIndex_NoEmptyFields() {
        for (int i = 0; i < 9; i++) {
            fieldMap.put(i, Sign.CROSS);
        }
        assertEquals(-1, field.getEmptyFieldIndex(), "No empty field should return -1");
    }

    @Test
    public void testCheckWin_Empty() {
        assertEquals(Sign.EMPTY, field.checkWin(), "Winner should be EMPTY");
    }

    @Test
    public void testCheckWin_Cross() {
        fieldMap.put(6, Sign.CROSS);
        fieldMap.put(7, Sign.CROSS);
        fieldMap.put(8, Sign.CROSS);

        assertEquals(Sign.CROSS, field.checkWin(), "Winner should be CROSS");
    }

    @Test
    public void testCheckWin_Nought() {
        fieldMap.put(2, Sign.NOUGHT);
        fieldMap.put(4, Sign.NOUGHT);
        fieldMap.put(6, Sign.NOUGHT);

        assertEquals(Sign.NOUGHT, field.checkWin(), "Winner should be NOUGHT");
    }

    @Test
    public void testGetFieldData_AllFieldsEmpty() {
        List<Sign> expectedData = Arrays.asList(
                Sign.EMPTY, Sign.EMPTY, Sign.EMPTY,
                Sign.EMPTY, Sign.EMPTY, Sign.EMPTY,
                Sign.EMPTY, Sign.EMPTY, Sign.EMPTY
        );

        assertEquals(expectedData, field.getFieldData(), "Field data should contain all EMPTY values");
    }

    @Test
    public void testGetFieldData_SomeFieldsFilled() {
        fieldMap.put(0, Sign.CROSS);
        fieldMap.put(1, Sign.NOUGHT);
        fieldMap.put(2, Sign.CROSS);

        List<Sign> fieldData = field.getFieldData();
        List<Sign> expectedData = Arrays.asList(
                Sign.CROSS, Sign.NOUGHT, Sign.CROSS,
                Sign.EMPTY, Sign.EMPTY, Sign.EMPTY,
                Sign.EMPTY, Sign.EMPTY, Sign.EMPTY
        );

        assertEquals(expectedData, fieldData, "Field data should reflect the filled values correctly");
    }
}