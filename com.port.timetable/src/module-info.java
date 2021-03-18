module com.port.timetable {
    exports com.port.timetable;
    requires com.google.gson;
    exports com.port.timetable.model;
    opens com.port.timetable.model;
}
