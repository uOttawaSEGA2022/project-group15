package com.example.ottawamealer;

public class MealRequest {
    Meal meal;
    enum Status {Pending,Accepted,Denied}
    Status status;
    String ID;
    String customer;

    public MealRequest(){}

    public MealRequest(Meal meal, long ID, String customer) {
        this.meal = meal;
        this.ID = ID+"";
        this.customer = customer;
    }

    public MealRequest(Meal meal) {
        this.meal = meal;
        status = Status.Pending;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * check if request is accepted
     * @return boolean
     */
    public boolean isAccepted(){
        return status == Status.Accepted;
    }

    /**
     * check if request is pending
     * @return
     */
    public boolean isPending(){
        return status == Status.Pending;
    }

    /**
     * check if request is denied
     * @return
     */
    public boolean isDenied(){
        return status ==Status.Denied;
    }

    //change from pending to accepted
    public void acceptRequest(){
        status = Status.Accepted;
    }

    public void declineRequest(){
        status = Status.Denied;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCustomer() {
        return customer;
    }
}
