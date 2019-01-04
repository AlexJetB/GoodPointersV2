package com.baker.goodpointersv2;

/**
 * @author Alexander Baker, <baker.alex.j@gmail.com>
 * Started 8 November, 2018
 * The pointer class holds information relevant to a pointer, such as the value and
 * address of the variable it is pointing to, as well as the name assigned to it.
 */
public class Pointer {
    private String pValue;
    private String pAddress;
    private String name;
    // Determine if pointer is dereferenced or not.
    private boolean pointing;
    private boolean derefed;

    // This will be the String to display in PointerVisualizer
    private String toDisplay;

    /**
     * Default constructor
     * Probably won't ever need this, but just to be safe.
     */
    public Pointer() {
        pValue = "";
        pAddress = "";
        name = "";
        toDisplay = pAddress;
        pointing = false;
        derefed = false;
    }

    /**
     * non-Default constructor for fully initialized pointer.
     * @param pointTo The nonPointer object being pointed to.
     * @param name The name of the pointer variable.
     */
    public Pointer(NonPointer pointTo, String name) {
        this.name = name;
        pAddress = pointTo.getAddress();
        pValue = pointTo.getValue();
        pointing = true;
        derefed = false;
        toDisplay = pAddress;
    }

    /**
     * non-Default constructor for non-initialized Pointer.
     * @param name The name to be assigned to the pointer.
     */
    public Pointer(String name) {
        this.name = name;
        pValue = "";
        pAddress = "";

        toDisplay = pAddress;
    }

    /**
     * Changes the toDisplay
     */
    public void deref(boolean state)
    {
        this.derefed = state;
        if (state == true) {
            this.toDisplay = pValue;
        } else {
            this.toDisplay = pAddress;
        }
    }

    // Standard getters and setters. Not vital for JavaDocs.
    // See: https://stackoverflow.com/questions/1028967/simple-getter-setter-comments
    public String getToDisplay() {
        return toDisplay;
    }

    public void setToDisplay(String toDisplay) {
        this.toDisplay = toDisplay;
    }

    public String getName() {
        return name;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public void setpValue(String pValue) {
        this.pValue = pValue;
    }

    public boolean isPointing() {
        return pointing;
    }

    public void setPointing(boolean pointing) {
        this.pointing = pointing;
    }

    public void setNonPointer(NonPointer newNonPointer) {
        setpValue(newNonPointer.getValue());
        setpAddress(newNonPointer.getAddress());
        setPointing(true);
        deref(false);
    }
}
