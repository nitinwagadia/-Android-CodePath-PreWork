package com.a2do.list.a2do.models;

import java.util.Date;

/**
 * Created by Nitin on 8/8/2017.
 */

public class ToDoItem {

    private String m_task_notes;
    private String m_status;
    private String m_priority;
    private String m_activity_name;
    private Date m_dueDate;

    public ToDoItem(String m_task_notes, String m_status, String m_priority, String m_activity_name, Date m_dueDate) {
        this.m_task_notes = m_task_notes;
        this.m_status = m_status;
        this.m_priority = m_priority;
        this.m_activity_name = m_activity_name;
        this.m_dueDate = m_dueDate;
    }

    public String get_task_notes() {
        return m_task_notes;
    }

    public void set_task_notes(String m_task_notes) {
        this.m_task_notes = m_task_notes;
    }

    public String get_status() {
        return m_status;
    }

    public void set_status(String m_status) {
        this.m_status = m_status;
    }

    public String get_priority() {
        return m_priority;
    }

    public void set_priority(String m_priority) {
        this.m_priority = m_priority;
    }

    public String get_activity_name() {
        return m_activity_name;
    }

    public void set_activity_name(String m_activity_name) {
        this.m_activity_name = m_activity_name;
    }

    public Date get_dueDate() {
        return m_dueDate;
    }

    public void set_dueDate(Date m_dueDate) {
        this.m_dueDate = m_dueDate;
    }


}
