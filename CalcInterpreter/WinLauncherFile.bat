:: ������� ������� �� ���������� Jav`�
:: http://habrahabr.ru/post/125210/

@echo off

:: �����(�������� �� ���������) // ��������

:: args_auto_end(true)// �������������� ������ END � ����� ��������� ������������������
:: auto_end(false) // �������������� ������ END � ����� ��������� ������������������
:: print_tokens(false) // ����� ��������� ������� ��� ���������������� ������
:: table // ������� ���������� � �������, �������� ������ �����, ����� ��. �������� ���������� del � ���������� ���������� add
:: precision(5) // ������������� ������� 10, ������������ ��� ��������� ����� �������� ������� doubleCompare()
:: errors(0) // ������� ��������� ������
:: stricted(true) // ������ ������������ ����������
:: auto_print(true) // �������������� ����� �������� ���������
:: greedy_func(false) // ������ �������: ������ �� �����������, ��, ��� �������� ����� ����� ������� � �� ������ 'END' ";" ��������� ���������� �������.
:: dim(deg) ����������� ��� �����(sin) � ������(arcsin) ������������������ �-��

java -classpath ./bin main.Executor "set auto_end= false; set greedy_func = false; set dim = deg; set stricted=false" < "in.txt" > "out.txt" 2> "err.txt"

::if ERRORLEVEL 1 pause
if ERRORLEVEL 1 (
start .\err.txt
pause
)