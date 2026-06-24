# University Management System

Консольна система управління університетом, створена мовою Java.

## Можливості

- CRUD-операції для студентів, викладачів і курсів;
- фільтрування та сортування студентів;
- фільтрування курсів;
- зарахування студентів на курси, оцінки та оплата;
- GPA, транскрипт, пошук і звіти;
- перевірка введених даних та обробка помилок.

Усі дані зберігаються в пам'яті програми у звичайних масивах.

## Структура

```text
src/university/
├── Main.java
├── entities/
├── enums/
├── interfaces/
├── services/
└── util/
```

## Компіляція і запуск

У PowerShell із кореневої папки проєкту:

```powershell
chcp 65001 | Out-Null
New-Item -ItemType Directory -Force out | Out-Null
$files = (Get-ChildItem -Recurse -LiteralPath src -Filter *.java).FullName
javac -encoding UTF-8 -d out $files
java "-Dfile.encoding=UTF-8" "-Dstdout.encoding=UTF-8" "-Dstderr.encoding=UTF-8" -cp out university.Main
```
