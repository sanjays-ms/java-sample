# SQL Server Express Setup Guide

This document explains how to configure SQL Server Express to work with Spring Boot application.

---

## 🎯 Problem
Spring Boot application couldn't connect to SQL Server Express with errors like:
- `Connection timeout`
- `Login failed for user`
- `Unable to load authentication DLL`

---

## ✅ Complete Solution

### **Step 1: Enable TCP/IP Protocol**

**Location:** SQL Server Configuration Manager

1. Open **SQL Server Configuration Manager**
   - Press `Windows + R`
   - Type: `SQLServerManager17.msc` (or your version)
   - Press Enter

2. Navigate to:
   ```
   SQL Server Network Configuration
   └── Protocols for SQLEXPRESS
   ```

3. Right-click **TCP/IP** → **Enable**
   - Status should change from "Disabled" to "Enabled"

---

### **Step 2: Configure TCP/IP Port**

1. Right-click **TCP/IP** → **Properties**

2. Go to **IP Addresses** tab

3. Scroll to the bottom → **IPAll** section

4. Set the port:
   ```
   TCP Dynamic Ports: [Leave empty or clear it]
   TCP Port: 1434
   ```

   **Note:** You can also use port `1433` (standard SQL Server port)

5. Click **OK**

---

### **Step 3: Enable Mixed Mode Authentication**

SQL Server Express by default uses Windows Authentication only. We need to enable SQL Server authentication.

1. Open **SQL Server Management Studio (SSMS)**

2. Connect to `localhost\SQLEXPRESS` (using Windows Authentication)

3. Right-click on **Server name** (at the very top) → **Properties**

4. Go to **Security** page (left panel)

5. Under "Server authentication", select:
   ```
   ☑ SQL Server and Windows Authentication mode
   ```

6. Click **OK**

7. **Important:** You'll see a message that SQL Server must be restarted for changes to take effect

---

### **Step 4: Create SQL Server Login**

1. In **SSMS**, expand **Security** → **Logins**

2. Right-click **Logins** → **New Login**

3. Fill in the details:
   ```
   Login name: sampleuser
   ☑ SQL Server authentication
   Password: sampleuser (or your chosen password)
   ☐ Enforce password policy (uncheck for development)
   ☐ Enforce password expiration (uncheck for development)
   ☐ User must change password at next login (uncheck)
   ```

4. **Important:** Go to **User Mapping** (left panel)

5. Check the box next to **sampledb** database

6. In the bottom panel, under "Database role membership for: sampledb":
   ```
   ☑ db_owner (full access to the database)
   ```

   **Other role options:**
   - `db_datareader` - Read-only access
   - `db_datawriter` - Read and write access
   - `db_owner` - Full control (recommended for development)

7. Click **OK**

---

### **Step 5: Restart SQL Server Service**

Changes require SQL Server to be restarted.

1. Press `Windows + R`
2. Type: `services.msc`
3. Press Enter
4. Find **SQL Server (SQLEXPRESS)**
5. Right-click → **Restart**
6. Wait for status to show "Running"

**Alternative (Command Line):**
```cmd
net stop MSSQL$SQLEXPRESS
net start MSSQL$SQLEXPRESS
```

---

### **Step 6: Update Spring Boot Configuration**

Update `src/main/resources/application.properties`:

```properties
# Database connection URL
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=sampledb;encrypt=false;trustServerCertificate=true;portNumber=1434

# SQL Server Authentication credentials
spring.datasource.username=sampleuser
spring.datasource.password=sampleuser

# Driver class (auto-detected, but explicit is clearer)
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

**Important Notes:**
- Double backslash `\\` in properties file becomes single `\` at runtime
- `encrypt=false` and `trustServerCertificate=true` for development only
- **Never commit passwords to version control in production!**

---

### **Step 7: Start SQL Server Browser (Optional)**

If using named instance (`SQLEXPRESS`) without specifying port:

1. In `services.msc`, find **SQL Server Browser**
2. Right-click → **Start**
3. (Optional) Right-click → **Properties** → Set Startup type to **Automatic**

---

## 🔍 Troubleshooting

### **Issue 1: Connection Timeout**
```
java.net.SocketTimeoutException: Receive timed out
```

**Solutions:**
- Check if SQL Server service is running
- Verify TCP/IP is enabled
- Check Windows Firewall (allow port 1434 or 1433)
- Verify SQL Server Browser is running (for named instances)

---

### **Issue 2: Login Failed**
```
Login failed for user 'sampleuser'
```

**Solutions:**
- Check if Mixed Mode authentication is enabled
- Verify username and password in application.properties
- Ensure user has permissions on sampledb database
- Restart SQL Server after enabling Mixed Mode

---

### **Issue 3: Integrated Authentication DLL Error**
```
Unable to load authentication DLL mssql-jdbc_auth
```

**Solutions:**
- Use SQL Server authentication instead of Windows authentication
- Remove `integratedSecurity=true` from connection string
- Add username and password to application.properties

---

## 📋 Quick Reference

### **Connection String Formats**

**Using Named Instance with Port:**
```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=sampledb;encrypt=false;trustServerCertificate=true;portNumber=1434
```

**Using Direct Port (No Instance Name):**
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=sampledb;encrypt=false;trustServerCertificate=true
```

**Using Windows Authentication (requires DLL):**
```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=sampledb;encrypt=false;trustServerCertificate=true;integratedSecurity=true
# No username/password needed
```

---

## ✅ Verification Steps

Test your connection:

1. **Start Spring Boot Application:**
   ```bash
   ./gradlew bootRun
   ```

2. **Look for success messages:**
   ```
   HikariPool-1 - Start completed
   Started DemoApplication in X seconds
   ```

3. **Check in SSMS:**
   ```sql
   USE sampledb;
   SELECT * FROM employees;
   ```

   Table should exist (created by Hibernate)

---

## 🔒 Security Best Practices

**For Development:**
- ✅ Simple passwords are okay
- ✅ Disable password policies for convenience
- ✅ `encrypt=false` for speed

**For Production:**
- ❌ Never use simple passwords
- ❌ Never commit passwords to Git
- ✅ Use environment variables or secret management
- ✅ Enable encryption (`encrypt=true`)
- ✅ Use strong password policies
- ✅ Limit database user permissions (not db_owner)
- ✅ Use separate accounts for different environments

---

## 📚 Additional Resources

- [Spring Boot Database Configuration](https://docs.spring.io/spring-boot/reference/data/sql.html)
- [SQL Server JDBC Driver Documentation](https://learn.microsoft.com/en-us/sql/connect/jdbc/)
- [SQL Server Authentication Modes](https://learn.microsoft.com/en-us/sql/relational-databases/security/choose-an-authentication-mode)

---