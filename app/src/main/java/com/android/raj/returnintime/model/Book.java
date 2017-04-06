package com.android.raj.returnintime.model;

public class Book {

    private String mTitle, mAuthor, mCheckedOutDate, mReturnDate, mReturnTo;

    public Book(String title, String author, String checkedOutDate, String returnDate) {
        mTitle = title;
        mAuthor = author;
        mCheckedOutDate = checkedOutDate;
        mReturnDate = returnDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getCheckedOutDate() {
        return mCheckedOutDate;
    }

    public void setCheckedOutDate(String checkedOutDate){
        mCheckedOutDate = checkedOutDate;
    }

    public String getReturnDate() {
        return mReturnDate;
    }

    public void setReturnDate(String returnDate) {
        mReturnDate = returnDate;
    }
//
//    public String getReturnTo() {
//        return mReturnTo;
//    }
//
//    public void setmReturnTo(String returnTo) {
//        mReturnTo = returnTo;
//    }
}
