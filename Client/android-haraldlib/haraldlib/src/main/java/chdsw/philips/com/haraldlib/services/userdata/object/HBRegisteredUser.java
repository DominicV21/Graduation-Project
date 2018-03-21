/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.object;

public class HBRegisteredUser {
    private int userIndex;
    //private int userNameLength;
    private int userNameTotalParts;
    private String[] userNameParts;
    private int userNamePartCounter;
    private boolean nameTruncated;

    private static final byte nameTruncatedFlag = 0x02;

    /**
     * Create a new registered user object without a name
     *
     * @param userIndex Index of the user
     */
    public HBRegisteredUser(int userIndex, int flags) {
        this.userIndex = userIndex;
        this.userNameTotalParts = 0;
        this.userNameParts = new String[0];
        this.nameTruncated = false;
    }

    /**
     * Create a new registered user object
     *
     * @param userIndex Index of the user
     * @param userNameParts Length of the name
     */
    public HBRegisteredUser(int userIndex, int userNameParts, int flags) {
        this.userIndex = userIndex;
        this.userNameTotalParts = userNameParts;
        this.userNameParts = new String[userNameParts];
        userNamePartCounter = 0;
        nameTruncated = ((flags & nameTruncatedFlag) == nameTruncatedFlag);
    }

    /**
     * Add a first name part to the first name array
     *
     * @param userNamePartNumber Part of the name
     * @param userNamePart Value of the part
     */
    public void addFirstNamePart(int userNamePartNumber, String userNamePart) {
        if(userNamePartNumber <= userNameTotalParts) {
            userNameParts[userNamePartNumber - 1] = userNamePart;
            userNamePartCounter++;
        }
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
    public String getUserName() {
        StringBuilder userNameBuilder = new StringBuilder();
        for (String userNamePart : userNameParts) {
            userNameBuilder.append(userNamePart);
        }
        if(nameTruncated) {
            userNameBuilder.append("...");
        }
        return userNameBuilder.toString();
    }

    /**
     * Is the first name complete
     *
     * @return Completion
     */
    public boolean isComplete() {
        if(userNameTotalParts == 0) {
            return true;
        }

        if(userNamePartCounter == userNameTotalParts) {
            return true;
        }
        return false;
    }
}