/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension.object;

public class HBRegisteredUser {
    private int userIndex;
    private int userNameLength;
    private String[] firstNameParts;

    /**
     * Create a new registered user object
     *
     * @param userIndex Index of the user
     * @param userNameLength Length of the name
     */
    public HBRegisteredUser(int userIndex, int userNameLength) {
        this.userIndex = userIndex;
        this.userNameLength = userNameLength;
        this.firstNameParts = new String[0];
    }

    /**
     * Add a first name part to the first name array
     *
     * @param userNamePart Part of the name
     * @param firstNamePart Value of the part
     */
    public void addFirstNamePart(int userNamePart, String firstNamePart) {
        if (firstNameParts.length <= userNamePart) {
            String[] newFirstNameParts = new String[userNamePart + 1];
            for (int i = 0; i < this.firstNameParts.length; i++) {
                newFirstNameParts[i] = this.firstNameParts[i];
            }
            this.firstNameParts = newFirstNameParts;
        }
        this.firstNameParts[userNamePart] = firstNamePart;
    }

    /**
     * Get the user index
     *
     * @return User index
     */
    public int  getUserIndex() {
        return userIndex;
    }

    /**
     * Get the first name
     *
     * @return First name
     */
    public String getFirstName() {
        StringBuilder firstNameBuilder = new StringBuilder();
        for (int i = 0; i < this.firstNameParts.length; i++) {
            firstNameBuilder.append(this.firstNameParts[i]);
        }
        return firstNameBuilder.toString();
    }

    /**
     * Is the first name complete
     *
     * @return Completion
     */
    public boolean isComplete() {
        return getFirstName().length() == userNameLength;
    }
}