# Плагин для Maven, проверяющий Google-таблицы на соответствие некоторым параметрам

*НЕОБХОДИМО ДОБАВИТЬ СКАЧАННЫЙ СО СТРАНИЦЫ GOOGLE API ФАЙЛ credentials.json*

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
              <credentials>${your.credentials.json.path}</credentials>
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
Доступные условия:
`empty`
`not_empty`
`equal`
`not_equal`
`less`
`greater`
`less_equal`
`greater_equal`
`not_less`
`not_greater`
`not_less_equal`
`not_greater_equal`
