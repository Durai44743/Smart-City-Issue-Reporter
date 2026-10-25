package com.smartcity.service;

import java.io.File;
import java.io.IOException;
import java.time.YearMonth;

public interface Reportable {
    File generateReport(YearMonth month) throws IOException;
}
