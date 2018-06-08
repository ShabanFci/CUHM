package com.fci.steps.cuhm;

/**
 * Created by Mohamed on 6/6/2018.
 */

public class Notifications {
    String first_name, last_name, problem, description_problem;


    public Notifications(String first_name, String last_name, String problem, String description_problem) {
        this.first_name = first_name;
        this.last_name = last_name;

        this.problem = problem;
        this.description_problem = description_problem;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDescription_problem() {
        return description_problem;
    }

    public void setDescription_problem(String description_problem) {
        this.description_problem = description_problem;
    }
}
