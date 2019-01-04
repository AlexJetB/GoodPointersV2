package com.baker.goodpointersv2;
/**
 * @author Alexander Baker, <baker.alex.j@gmail.com>
 * Started 8 November, 2018
 * This class will instantiate NonPointer variable types as needed by the
 * PointerVisualizer and VisController classes.
 * They will hold date typical to a C++ variable.
 */
public class NonPointer {
    private String address;
    private String value;
    private String name;

    /**
     * Default constructor
     * Probably won't ever need this, but just to be safe.
     */
    public NonPointer() {
        address = "0x";
        value = "";
        name = "";
    }

    /**
     * Non-default Constructor of initialized non-pointer.
     * e.g. in C++
     *      int foo = 25;
     * @param name The name of the variable.
     * @param value The value of the variable.
     */
    public NonPointer(String name, String value) {
        address = "0x";
        this.value = value;
        this.name = name;
    }

    /**
     * Non-default Constructor of non-initialized pointer.
     * e.g. in C++
     *      int foo;
     * @param name The name of the variable.
     */
    public NonPointer(String name) {
        address = "0x";
        value = "";
        this.name = name;
    }

    /**
     * Relatively simple address generator for 4 digit addresses.
     * There is a "base" table location of 0x, but then append
     * the array location from PointerVisualizer.
     * Addresses in most computers are displayed in an indexed form, AKA
     *
     * @param myPlace The data table location as given by the VisController
     */
    public void addressAppend(Integer myPlace) {
        address += myPlace;
    }

    // Standard getters and setters. JavaDccs aren't important here.
    // See: https://stackoverflow.com/questions/1028967/simple-getter-setter-comments
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
