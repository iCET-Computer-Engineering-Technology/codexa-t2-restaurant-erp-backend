# Quick Reference: Running Tests & Troubleshooting

## ⚡ 30-Second Quick Start

```bash
# Navigate to project
cd "C:\iCD119 Weekdays\Industry Training\codexa-t2-restaurant-erp-backend"

# Run all tests
mvn clean test

# Run only OrderServiceImplTest
mvn clean test -Dtest=OrderServiceImplTest

# View test results
# Results will appear in console output
```

## 🎯 Common Commands

### Run Everything
```bash
mvn clean test
```
**Output**: Shows all test results, pass/fail counts

### Run Specific Test Class
```bash
mvn test -Dtest=OrderServiceImplTest
```
**Output**: Only OrderServiceImplTest execution

### Run Specific Test Method
```bash
mvn test -Dtest=OrderServiceImplTest#testCreateOrderSuccessfullyDineIn
```
**Output**: Single test execution with detailed output

### Generate Coverage Report
```bash
mvn clean test jacoco:report
```
**Output**: 
- Report generated in: `target/site/jacoco/index.html`
- Shows line coverage, branch coverage, method coverage

### Skip Tests During Build
```bash
mvn clean install -DskipTests
```
**Use when**: You want to build without running tests

### Run with Detailed Output
```bash
mvn clean test -X
```
**Output**: Verbose logging with all debug info

## 📊 Understanding Test Output

### Successful Run
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running edu.icet.ecom.service.impl.OrderServiceImplTest
[INFO] Tests run: 75, Failures: 0, Errors: 0, Skipped: 0
[INFO] -------------------------------------------------------
[INFO] BUILD SUCCESS
```

### Failed Test
```
[INFO] Tests run: 75, Failures: 1, Errors: 0, Skipped: 0
[ERROR] testCreateOrderSuccessfullyDineIn FAILED
[ERROR] Expected: 100.00
[ERROR] Got: 50.00
```
**Action**: Review test assertion and implementation

### Build Error
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.8.1:testCompile
[ERROR] COMPILATION ERROR
```
**Action**: Check for syntax errors in test file or missing dependencies

## 🔧 Troubleshooting

### Problem: "Cannot resolve symbol 'junit'"
```
[ERROR] Cannot resolve symbol 'junit'
```
**Causes**:
- Maven dependencies not downloaded
- IDE cache outdated

**Solutions**:
1. Clear Maven cache: `mvn clean`
2. Force update: `mvn clean install -U`
3. Reload IDE project
4. Run: `mvn dependency:resolve`

### Problem: "BUILD FAILURE" with dependency error
```
[ERROR] Failed to resolve dependencies
```
**Solutions**:
1. Check internet connection
2. Verify pom.xml is correct
3. Run: `mvn dependency:resolve -X`
4. Check Maven repository availability

### Problem: Tests won't run
```
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0
```
**Solutions**:
1. Verify test file location: `src/test/java/edu/icet/ecom/service/impl/OrderServiceImplTest.java`
2. Ensure test class name matches file name
3. Verify `@Test` annotations present
4. Check for typos in test method names

### Problem: "Mock not working" / "NullPointerException"
```
NullPointerException at OrderServiceImplTest.java:126
```
**Solutions**:
1. Verify `@ExtendWith(MockitoExtension.class)` on class
2. Check `@Mock` annotations present
3. Verify `@InjectMocks` on service under test
4. Ensure `when()` setup before test execution

### Problem: "Cannot find test class"
```
[ERROR] No tests were executed!
```
**Solutions**:
1. Verify file exists: `src/test/java/...OrderServiceImplTest.java`
2. Check class name exactly matches file name
3. Ensure package declaration correct
4. Run: `mvn test-compile`

## 📈 CI/CD Integration Examples

### GitHub Actions
```yaml
name: Run Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Run tests
        run: mvn clean test
```

### GitLab CI
```yaml
test:
  image: maven:3.8.1-jdk-11
  script:
    - mvn clean test
  artifacts:
    paths:
      - target/site/jacoco/
```

### Jenkins
```groovy
pipeline {
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
}
```

## 📚 IDE Integration

### IntelliJ IDEA / JetBrains
1. Right-click test class → Run Tests
2. Right-click test method → Run Test
3. Use `Ctrl+Shift+F10` to run current test
4. Use `Ctrl+Shift+F9` to run test class

### Eclipse
1. Right-click test class → Run As → JUnit Test
2. Right-click test method → Run As → JUnit Test
3. View results in JUnit view

### VS Code
1. Install "Test Runner for Java" extension
2. Click play icon next to test name
3. View results in Output terminal

## 🎓 Test Reports & Analysis

### View HTML Report
```bash
# Generate report
mvn clean test jacoco:report

# Open in browser (Windows)
start target\site\jacoco\index.html

# Open in browser (Mac)
open target/site/jacoco/index.html

# Open in browser (Linux)
xdg-open target/site/jacoco/index.html
```

### Coverage Metrics Explained
- **Line Coverage**: % of code lines executed
- **Branch Coverage**: % of decision branches taken
- **Method Coverage**: % of methods called
- **Instruction Coverage**: % of bytecode instructions

**Target**: >90% line coverage ✅

## 🛠️ Advanced Options

### Run Tests in Parallel
```bash
mvn test -DthreadCount=4
```

### Run with Specific Profile
```bash
mvn test -Pdev
```

### Run with System Properties
```bash
mvn test -Dproperty.name=value
```

### Generate Surefire Report
```bash
mvn surefire-report:report
```

## 📋 Pre-Test Checklist

- ✅ Java 11+ installed (`java -version`)
- ✅ Maven 3.8+ installed (`mvn -version`)
- ✅ Test file exists: `src/test/java/edu/icet/ecom/service/impl/OrderServiceImplTest.java`
- ✅ pom.xml has test dependencies (JUnit 5, Mockito)
- ✅ Internet connection (for Maven dependencies)
- ✅ IDE properly configured (if using IDE)

## 📝 Post-Test Steps

1. **Review Results**
   - Check console output
   - Look for failures/errors

2. **Analyze Coverage**
   - Run: `mvn jacoco:report`
   - Open: `target/site/jacoco/index.html`
   - Target: >90% coverage

3. **Fix Failures** (if any)
   - Read error messages carefully
   - Check test vs implementation
   - Verify mock setup

4. **Commit Results** (if successful)
   - Add test file to git
   - Update pom.xml
   - Create commit with message

## 🚀 Next: Integration Testing

After unit tests pass:

```bash
# Create integration tests
mkdir src/test/java/edu/icet/ecom/integration/
```

```java
@SpringBootTest
@DataJdbcTest
class OrderServiceIntegrationTest {
    // Use real database (H2/MySQL)
    // Test complete workflows
}
```

## 📞 Support Resources

- **Maven Documentation**: https://maven.apache.org/
- **JUnit 5 Guide**: https://junit.org/junit5/docs/
- **Mockito Documentation**: https://javadoc.io/doc/org.mockito/mockito-core
- **Spring Boot Testing**: https://spring.io/guides/gs/testing-web/

---

**Last Updated**: April 1, 2026  
**Status**: Ready to Execute ✅

