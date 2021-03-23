module com.port.controller {
    requires com.port.timetable;
    requires com.port.port;
    requires com.google.gson;
    exports com.port.controller;
    exports com.port.controller.model;
    opens com.port.controller;
}
