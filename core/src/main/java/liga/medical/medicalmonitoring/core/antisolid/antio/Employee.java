package liga.medical.medicalmonitoring.core.antisolid.antio;

class Employee extends Person {

    @Override
    public String sayHello() {
        return super.sayHello() + "Employee";
    }

}
