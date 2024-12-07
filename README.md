Introducción


En este laboratorio se utilizó el framework Drools, un motor de reglas de negocio en Java que permite parametrizar reglas de negocio de manera externa a las aplicaciones. Esto facilita la separación de lógica y datos, promoviendo un diseño más modular y flexible. A través de este laboratorio, se implementó un sistema para evaluar solicitudes de crédito en una aplicación basada en Spring Boot, demostrando el uso de reglas de negocio configuradas en un archivo DRL.
Objetivos


Comprender el concepto y la arquitectura de Drools como sistema de administración de reglas de negocio
Implementar reglas de negocio utilizando el lenguaje DRL.
Integrar Drools con una aplicación Spring Boot para evaluar casos prácticos.
Configurar un sistema que evalúe la viabilidad de créditos bancarios basados en criterios predefinidos.
Promover la programación declarativa mediante el uso de Drools.
Proceso


1. Configuración del proyecto
Se creó una aplicación Spring Boot utilizando Spring Initializer con las dependencias necesarias, como spring-boot-starter-web, lombok y las librerías de Drools.


El archivo pom.xml se configuró para incluir las versiones compatibles con Spring Boot


3. Implementación de la configuración
Se desarrolló una clase de configuración DroolsConfig que inicializa el KieContainer, responsable de cargar y compilar las reglas definidas en el archivo loan_rate.drl.


@Configuration
public class DroolsApplicationConfig {
   private static final KieServices kieServices= KieServices.Factory.get();
   private static final String RULES_CUSTOMER_RULES_DRL = "rules/loan_rate.drl";
   @Bean
   public KieContainer kieContainer(){
       KieFileSystem kieFileSystem = KieServices.get().newKieFileSystem();
       kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_CUSTOMER_RULES_DRL
       ));


       KieBuilder kb= kieServices.newKieBuilder(kieFileSystem);
       kb.buildAll();
       KieModule kieModule = kb.getKieModule();
       KieContainer kieContainer =
               kieServices.newKieContainer(kieModule.getReleaseId());
       return kieContainer;
   }
}


3. Definición de modelos
Se crearon las clases Participant y Rate para representar los datos de entrada y salida del motor de reglas:
Participant: contiene información como nombre, edad, puntaje crediticio, salario anual, deudas existentes, y monto del préstamo solicitado.
Rate: almacena el estado del crédito (aprobado o rechazado) y la tasa de interés correspondiente.


public class Participant {
   private String name;
   private int age;
   private int creditScore;
   private long annualSalary;
   private long existingDebt;
   private long loanAmount;
   private String employmentType; // Full-time, Part-time, Self-employed, Unemployed
   private int yearsWithBank; // Years as a bank client
   private boolean hasSavingsAccount;
   private long investmentPortfolioValue; // Value of investment portfolio
   private long monthlyExpenses;


4. Definición de reglas


Las reglas se especificaron en el archivo loan_rate.drl ubicado en el directorio resources/rules. Las condiciones evalúan aspectos como:
Relación entre deuda existente y monto del préstamo.
Salario anual del solicitante.
Puntaje crediticio en diferentes rangos.


import com.udea.reglas.model.Participant;
global com.udea.reglas.model.Rate rate;
dialect "mvel"


// Regla 1: Rechazar si la deuda existente excede el 50% del salario anual
rule "High Debt to Salary Ratio"
when
   Participant(existingDebt > (0.5 * annualSalary))
then
   rate.setLoanStatus("Rejected - High Debt to Salary Ratio");
   rate.setLoanRate(999);
end


// Regla 2: Ofrecer tarjeta de crédito premium para clientes con buen puntaje y antigüedad
rule "Premium Credit Card Offer"
when
   Participant(creditScore >= 700, yearsWithBank >= 5, hasSavingsAccount == true)
then
   rate.setLoanStatus("Rejected - High Debt to Salary Ratio");
   rate.setLoanRate(999);
end


// Regla 3: Ofrecer cuenta de ahorros a jóvenes sin cuenta actual
rule "Youth Savings Account Offer"
when
   Participant(age < 25, hasSavingsAccount == false)
then
   rate.setLoanStatus("Rejected - High Debt to Salary Ratio");
   rate.setLoanRate(999);
end


// Regla 4: Rechazar si el puntaje de crédito es menor a 500, independientemente de otros factores
rule "Reject Very Low Credit Score"
when
   Participant(creditScore < 500)
then
   rate.setLoanStatus("Rejected - Very Low Credit Score");
   rate.setLoanRate(999);
end


// Regla 5: Ofrecer portafolio de inversión a clientes con alto valor de inversiones
rule "Investment Portfolio Offer"
when
   Participant(investmentPortfolioValue > 50000)
then
   rate.setLoanStatus("Approved - Small Loan");
   rate.setLoanRate(8.5);
end


// Regla 6: Rechazar si los gastos mensuales superan el 70% del salario mensual
rule "High Monthly Expenses"
when
   Participant(monthlyExpenses > (0.7 * (annualSalary / 12)))
then
   rate.setLoanStatus("Rejected - High Monthly Expenses");
   rate.setLoanRate(999);
end


// Regla 7: Ofrecer préstamos pequeños a clientes con empleo parcial y buen historial
rule "Small Loan for Part-Time Employees"
when
   Participant(employmentType == "Part-time", creditScore >= 650, loanAmount <= 5000)
then
   rate.setLoanStatus("Approved - Small Loan");
   rate.setLoanRate(8.5);
end


// Regla 8: Ofrecer seguro de vida a clientes con deuda alta y sin cuenta de ahorros
rule "Life Insurance Offer"
when
   Participant(existingDebt > 50000, hasSavingsAccount == false)
then
   rate.setLoanStatus("Approved - Small Loan");
   rate.setLoanRate(8.5);
end


// Regla 9: Aprobar préstamos grandes solo a clientes con empleo de tiempo completo y buen puntaje
rule "Large Loan for Full-Time Employees"
when
   Participant(employmentType == "Full-time", creditScore >= 750, loanAmount > 20000)
then
   rate.setLoanStatus("Approved - Large Loan");
   rate.setLoanRate(5.5);
end


// Regla 10: Ofrecer educación financiera a clientes con puntaje de crédito bajo
rule "Financial Education Offer"
when
   Participant(creditScore < 600)
then
   rate.setLoanStatus("Approved - Small Loan");
   rate.setLoanRate(8.5);
end


5. Evaluación del sistema
Las reglas fueron aplicadas a instancias de Participant para determinar el estatus del crédito y la tasa de interés correspondiente. Las evaluaciones se realizaron utilizando una memoria de trabajo en Drools, donde se cargaron los datos de entrada.


{
    "name": "Test User",
    "age": 20,
    "creditScore": 650,
    "annualSalary": 90000,
    "existingDebt": 1000,
    "loanAmount": 400,
    "employmentType": "Full-time",
    "yearsWithBank": 3,
    "hasSavingsAccount": false,
    "investmentPortfolioValue": 20000,
    "monthlyExpenses": 1500
}


Con postman se hace la solicitud POST en la url especificada


Análisis


El uso de Drools en este sistema permitió:


Separación de lógica y datos: Las reglas de negocio se gestionaron de forma independiente al código de la aplicación, lo que facilita su actualización sin afectar otras partes del sistema.
Flexibilidad y reutilización: Al definir reglas declarativas y atómicas, se simplificó su aplicación a diferentes objetos y contextos


Eficiencia en la evaluación: Gracias al uso del algoritmo Rete, las reglas se aplicaron de manera eficiente sobre los datos cargados en la memoria de trabajo.
Se deben tener en cuenta los siguientes aspectos:


Curva de aprendizaje: La sintaxis de DRL y la integración con Spring Boot requieren un conocimiento previo de Java y programación declarativa


Compatibilidad de versiones: La configuración adecuada de las dependencias es crucial para evitar problemas de incompatibilidad


Puntos a Tener en Cuenta
Estructura clara de las reglas: definir reglas concisas y evitar condicionales complejos mejora la mantenibilidad


Testing exhaustivo: verificar las reglas con casos de prueba variados asegura la correcta implementación de las políticas de negocio


Monitorización de rendimiento: en sistemas con un alto volumen de datos, es importante evaluar el rendimiento del motor Drools
Documentación de reglas: mantener un registro claro de las reglas definidas para facilitar su comprensión y modificación.


Conclusiones
El uso de Drools como motor de reglas de negocio demuestra ser una herramienta poderosa para gestionar políticas empresariales en aplicaciones dinámicas. Su capacidad de separación entre lógica y datos, junto con su flexibilidad, lo convierte en una solución ideal para escenarios donde las reglas de negocio son cambiantes. Este laboratorio permitió no solo implementar un sistema funcional para evaluación de créditos, sino también comprender los beneficios y retos asociados con el uso de motores de reglas en entornos empresariales.
