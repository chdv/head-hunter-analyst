# Аналитика хед-хантера
Простенькое приложение, которое создавалась для реализации двух задач - упрощения поиска вакансий по определенным ключевым словам на сайте hh.ru. 
В настоящий момент реализовано сохранение данных по запросу в файл. Ключевые слова запроса указываются в конфигурационном файле analyst-config.xml, секция "key-world". Результат выполнения сохраняется в локальном html-файле. Запросы к hh.ru и наименование создаваемого файла выводятся в лог.
