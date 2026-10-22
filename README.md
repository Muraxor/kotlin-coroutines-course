# Kotlin Coroutines — практический курс

10 самостоятельных Gradle-модулей и 120 задач по возрастанию сложности. Каждый модуль содержит краткую теорию **на русском и английском** с точными ссылками на официальную документацию Kotlin/JetBrains. Полные тексты документации не копируются. Учебные объяснения и задачи составлены для этого проекта.

## Начать

Нужна JDK 21. Открой папку проекта в IntelliJ IDEA как Gradle-проект (Gradle JVM = 21) или работай в терминале:

```bash
./gradlew :01-basics:task01 --console=plain
```

На Windows используй `gradlew.bat`. Первый запуск скачивает Gradle и зависимости. Все команды выполняй из корневой папки проекта.

## Формат занятий

1. Прочитай `THEORY.md` или `THEORY_EN.md` выбранного модуля. При необходимости открой официальные разделы по ссылкам.
2. Открой `TASKS.md`: там условия написаны словами, критерии результата и точная команда запуска.
3. Измени отдельный файл `src/main/kotlin/course/TaskNN.kt` и запусти его. Первые четыре упражнения дают код для правки; следующие четыре — заготовку; в последних четырёх нужно самостоятельно собрать механизм корутин. Начиная со второго модуля входные данные, имитация работы, консольные сообщения и проверка результата уже подготовлены.
4. Смотри на консоль: метки времени, поток, порядок событий, значения и `OK`/`ПОКА НЕ ГОТОВО`. Успешная сборка сама по себе не означает правильное решение.
5. Если застрял, открой `SOLUTIONS.md` модуля: код, что именно сделать, почему это работает и ожидаемый результат. По непонятным моментам спрашивай здесь, в чате.

Сложность растёт внутри каждого модуля: 01–04 знакомят с одним приёмом, 05–08 требуют применить его в небольшом сценарии, 09–11 добавляют самостоятельный выбор способа решения, а 12-я задача соединяет несколько требований. Следующие модули опираются на предыдущие: например, итоговая задача про диспетчеры использует одновременный запуск из модуля 02, а задачи про Flow, горячие потоки, каналы и тестирование соединяют обработку ошибок, отмену и безопасное общее состояние. Названия нужных корутинных функций в условиях итоговых задач не подсказываются.

Каждый файл запускается отдельно. Например, для задачи 7 второго модуля:

```bash
./gradlew :02-results:task07 --console=plain
```

Пример консоли после решения первой задачи:

```text
[    0 ms] [main                ] === Задача 01.01 ===
[    5 ms] [main                ] Начало
[  210 ms] [main                ] Конец
[  211 ms] [main                ] === Завершено 01.01 ===
```

Миллисекунды и имена рабочих потоков могут отличаться. Сравнивай причинный порядок и значения.

## Модули

| Модуль | Теория | Практика | Ответы |
|---|---|---|---|
| [01-basics](01-basics/README.md) | [русский](01-basics/THEORY.md) / [English](01-basics/THEORY_EN.md) | [12 задач](01-basics/TASKS.md) | [решения](01-basics/SOLUTIONS.md) |
| [02-results](02-results/README.md) | [русский](02-results/THEORY.md) / [English](02-results/THEORY_EN.md) | [12 задач](02-results/TASKS.md) | [решения](02-results/SOLUTIONS.md) |
| [03-dispatchers](03-dispatchers/README.md) | [русский](03-dispatchers/THEORY.md) / [English](03-dispatchers/THEORY_EN.md) | [12 задач](03-dispatchers/TASKS.md) | [решения](03-dispatchers/SOLUTIONS.md) |
| [04-cancellation](04-cancellation/README.md) | [русский](04-cancellation/THEORY.md) / [English](04-cancellation/THEORY_EN.md) | [12 задач](04-cancellation/TASKS.md) | [решения](04-cancellation/SOLUTIONS.md) |
| [05-errors](05-errors/README.md) | [русский](05-errors/THEORY.md) / [English](05-errors/THEORY_EN.md) | [12 задач](05-errors/TASKS.md) | [решения](05-errors/SOLUTIONS.md) |
| [06-shared-state](06-shared-state/README.md) | [русский](06-shared-state/THEORY.md) / [English](06-shared-state/THEORY_EN.md) | [12 задач](06-shared-state/TASKS.md) | [решения](06-shared-state/SOLUTIONS.md) |
| [07-flow](07-flow/README.md) | [русский](07-flow/THEORY.md) / [English](07-flow/THEORY_EN.md) | [12 задач](07-flow/TASKS.md) | [решения](07-flow/SOLUTIONS.md) |
| [08-hot-flow](08-hot-flow/README.md) | [русский](08-hot-flow/THEORY.md) / [English](08-hot-flow/THEORY_EN.md) | [12 задач](08-hot-flow/TASKS.md) | [решения](08-hot-flow/SOLUTIONS.md) |
| [09-channels](09-channels/README.md) | [русский](09-channels/THEORY.md) / [English](09-channels/THEORY_EN.md) | [12 задач](09-channels/TASKS.md) | [решения](09-channels/SOLUTIONS.md) |
| [10-testing-project](10-testing-project/README.md) | [русский](10-testing-project/THEORY.md) / [English](10-testing-project/THEORY_EN.md) | [12 задач](10-testing-project/TASKS.md) | [решения](10-testing-project/SOLUTIONS.md) |

## Проверка сборки

```bash
./gradlew classes --console=plain
```

Версии: Kotlin 2.3.20, kotlinx.coroutines 1.11.0, Gradle 9.3.1, JDK 21. `kotlinx-coroutines-test` добавлен в runtime только десятого модуля, чтобы его учебные задачи с `runTest` можно было наблюдать в консоли. В приложениях библиотека тестирования обычно нужна только в тестовых зависимостях.
