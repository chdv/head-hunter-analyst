package com.dch.app.analyst.util;

import java.util.concurrent.Executor;

/**
 * ������� ��������� ��� ������� ���������� ����� � ������������ �������� �� ����������
 * Created by ������� on 14.06.2015.
 */
public interface ForkJoinRunner extends Executor {

    void awaitJobsDone();
}
