# Плагин для Maven, проверяющий Google-таблицы на соответствие некоторым параметрам

*НЕОБХОДИМО ДОБАВИТЬ СКАЧАННЫЙ СО СТРАНИЦЫ GOOGLE API ФАЙЛ credentials.json В ПАПКУ resources*

Пример использования:

```XML
<plugins>
      <plugin>
        <groupId>cherniykot.plugins</groupId>
        <artifactId>excel-validation-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <executions>
          <execution>
            <id>execution1</id>
            <phase>validate</phase>
            <goals>
              <goal>check</goal>
            </goals>
            <configuration>
              <spreadsheetId>1Ry739nTeDgEDXIxVIJZG8SUlKCKeohxOHn3AKsIxJ_w</spreadsheetId>
              <conditions>
                <condition>
                  <cell>Поток 1.6!M12</cell>
                  <condition>greater_equal</condition>
                  <value>2</value>
              </conditions>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
```
