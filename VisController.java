package com.baker.goodpointersv2;

import android.util.Log;

import java.util.List;

/**
 * @author Alexander Baker baker.alex.j@gmail.com
 * Started 26 November 2018
 * This class will act as the "control" (like in the MVC pattern)
 * of the custom view, PointerVisualizer (which is the view).
 * It will mainly manipulate the two ListArrays within PointerVisualizer,
 * by adding, removing, and changing their data.
 *
 * pointer type variables - [color] Circle
 * non-pointer type variables - Stacked rectangles
 *                              upper = Address as [color] rect
 *                              lower = Value as [color] rect
 * The Controller defines the custom view defined in:
 *       PointerVisualizer.java
 * Using data obtained from the class
 *       CppMemory.java
 */

/**
 *
 */
public class VisController {
    private com.baker.goodpointersv2.PointerVisualizer pointerVisualizer;
    private Integer fauxAddressPlace = 0;

    /**
     * Constructor
     * @param pointerVisualizer pointerVisualizer view class to modify.
     */
    public VisController(com.baker.goodpointersv2.PointerVisualizer pointerVisualizer) {
        this.pointerVisualizer = pointerVisualizer;
    }

    /**
     * This function is called whenever the view needs to be updated from the
     * CppMemory variable.
     * I will handle any state changes or variable additions.
     */
    public void update() {
    }

    /**
     * Add a new pointer to the array.
     * @param newPtr
     */
    public void addPointer(Pointer newPtr) {
        // Error checking, make sure name is unique, not a copy.
        if (findPointerByName(newPtr.getName()) == -1) {
            pointerVisualizer.ptrList.add(newPtr);
        } else {
            // TODO: Implement toast for this error message.
            Log.e("VISUAL CONTROLLER: ", "Cannot add two ptr variables of the same name!");
        }
    }

    /**
     * Add a new nonPointer to the array.
     * @param newNonPtr
     */
    public void addNonPointer(NonPointer newNonPtr) {
        if (findNonPointerByName(newNonPtr.getName()) == -1) {
            newNonPtr.addressAppend(fauxAddressPlace);
            pointerVisualizer.nonPtrList.add(newNonPtr);
            fauxAddressPlace++;
        } else {
            Log.e("VISUAL CONTROLLER: ", "Cannot add two variables of the same name!");
        }
    }

    /**
     * Assign a new value to the specified nonPointer.
     * @param nonPtrName
     * @param newValue
     */
    public void changeNonPtrVal(String nonPtrName, String newValue) {
        int index = findNonPointerByName(nonPtrName);
        if (index != -1 && newValue != null) {
            pointerVisualizer.nonPtrList.get(index).setValue(newValue);
        } else {
            Log.e("VISUAL CONTROLLER: ","Cannot change a non existant variable!");
        }
    }

    public void changePtrVal(String ptrName, String nonPtrName) {
        int pIndex = findPointerByName(ptrName);
        int index = findNonPointerByName(nonPtrName);
        if (pIndex != -1 && index != -1) {
            pointTo(ptrName, nonPtrName);
        } else {
            Log. e("VISUAL CONTROLLER:", "Cannot change a non existent variable!");
        }
    }

    /**
     * Function used to set pointers that where not initialized.
     * (Pointing to a nonPointer variable) Possible update method for any changes made.
     * @param pointerName The name of the variable.
     * @param nonPointerName The name of the variable.
     */
    public void pointTo(String pointerName, String nonPointerName) {
        int iOfPtr;
        int iOfNonPtr;
        String pntToAddress;
        String pntToValue = "";

        if (pointerVisualizer.ptrList.size() > 0
                && pointerVisualizer.nonPtrList.size() > 0) {
            iOfPtr = findPointerByName(pointerName);
            iOfNonPtr = findNonPointerByName(nonPointerName);

            // Check if both names are in the list.
            if (iOfPtr != -1 && iOfNonPtr != -1) {
                NonPointer newNP = pointerVisualizer.nonPtrList.get(iOfNonPtr);
                pointerVisualizer.ptrList.get(iOfPtr).setNonPointer(newNP);
            } else {
                // Error message, possibly a toast?
                if (iOfPtr == -1) {
                    Log.e("VISUAL CONTROLLER: ",
                            "Cannot find pointer" + pointerName + "in list.");
                } else {
                    Log.e("VISUAL CONTROLLER: ",
                            "No NONpointer" + nonPointerName + "in list.");
                }
            }
        }
    }

    /**
     * Searches for a pointer in the pointer ListArray given its name.
     * @param pointerName The name of the variable.
     * @return If -1, pointer not found.
     */
    public int findPointerByName(String pointerName) {
        int indexPlace = -1;

        for(int i = 0; i < pointerVisualizer.ptrList.size(); i++) {
            if (pointerVisualizer.ptrList.get(i).getName().equals(pointerName)) {
                indexPlace = i;
            }
        }

        return indexPlace;
    }

    /**
     * Searches for a nonPointer in the nonPointer ListArray given its name.
     * @param nonPointerName The name of the variable.
     * @return If -1, nonPointer not found.
     */
    public int findNonPointerByName(String nonPointerName) {
        int indexPlace = -1;

        for(int i = 0; i < pointerVisualizer.nonPtrList.size(); i++) {
            if (pointerVisualizer.nonPtrList.get(i).getName().equals(nonPointerName)) {
                indexPlace = i;
            }
        }
        return indexPlace;
    }

    /*public void derefPointer(Pointer toDeref) {

    }*/

    /**
     * Remove a specific nonPointer from the list array.
     * @param toDeleteName name of nonPointer to remove.
     */
    public void deleteNonPointer(String toDeleteName) {
        int i = findNonPointerByName(toDeleteName);

        if (i != -1) {
            pointerVisualizer.nonPtrList.remove(i);
        }
    }

    //Will likely not be used for now
    /**
     * Remove a specific pointer from the list array.
     * @param toDeleteName name of pointer to remove.
     */
    public void deletePointer(String toDeleteName) {
        int i = findPointerByName(toDeleteName);

        if (i != -1) {
            pointerVisualizer.ptrList.remove(i);
        }
    }
}
