//this file presents two basic examples of the creation and usage of complex data types
//until not told otherwise remove the code below before creating your solutions

import static java.lang.IO.*;  //including package IO to be able to use simple print()
import static java.lang.IO.println;
import static term.term.*;     //includes package term (createElements() functions were moved there)


//***************************************************
//        Example A. Complex data types
//***************************************************

public static class TPoint {   //types need to be static to work as records while being internal classes (inside other class)
    int x;                 //as we are using simplified syntax, then it is the only way (we can not move them outside, as the class Main is hidden)
    int y;                 //to make them non-static, they would have to be in separate files (each in separate file - not handy now)
}                      //types need to be public to allow createElements() to access them from the other package

public static class TPointsAroundCenter {
    TPoint[] points = new TPoint[40];  //all arrays must have their size set to allow automatic memory allocation with createElements()
    TPoint central_point;              //this will also be allocated by createElements()
}

;

public static class TAlienRobot {      //just another "random" record type for presentation how to use createElements()
    TPoint[] visited_places = new TPoint[100];  //Remark: it would be good to create constants 
    TPoint[] targets = new TPoint[10];          //    representing sizes of arrays - "magic values" are not recommended
    TPointsAroundCenter[] danger_zones = new TPointsAroundCenter[4];
    TPointsAroundCenter bot_structure; // = new TPointsAroundCenter();  //no need to use automatic constructor here, createElements() will handle this
    int[] plasma_containers = new int[7];
    int max_speed;
    TAlienRobot sub_bot;    //Alien robot can include a robot which can include a robot... This is an alien tech. (recursive structure, do not use such things now)
    String name;
    String[] access_passwords = new String[10];
}

;

//helper function to create point from x and y - such functions can be very useful to create elements from data
static TPoint point(int x, int y) {
    TPoint p = new TPoint();
    p.x = x;
    p.y = y;
    return p;
}

//***************************************************
//     Example A. Creation of example variables
//***************************************************

int[] solar_wind_levels = new int[10];   //all elements will be allocated automatically by Java (no need to use createElements() for arrays of primitive types)
TPoint[] lightning_locations = new TPoint[20];   //reference to array is created but elements of complex type are not created -> createElements(lightning_locations) needed
TPointsAroundCenter proposed_targets;  //not initialised at all => proposed_targets is null; proposed_targets=createElements(TPointsAroundCenter.class) can handle this
TAlienRobot abot;

//Example A. Simple test of memory allocation and read/write access to created structures
//example how to easily create and access nested structures defined above
void StructInitTest() {
    println("Testing allocation of example structures");
    println();
    //createElements(solar_wind_levels);  //this can be run but there is no need, as solar_wind_levels array is already initialised with its elements (primitive types are initialised automatically)
    //solar_wind_levels = createElements(int[].class);  //such approach will not work as size of an array will not be known
    solar_wind_levels[0] = 0;
    solar_wind_levels[1] = 2;
    solar_wind_levels[2] = 7;
    println(">>> Local arrays of primitive types: OK");

    createElements(lightning_locations); //b is not null so we can analyse its type and allocate memory if needed
    lightning_locations[1].x = 0;
    lightning_locations[1].y = 1;
    println(">>> Local arrays of complex types: OK");

    //createElements(proposed_targets);  //this will not work because proposed_targets==null (and type can not be revealed from null)
    proposed_targets = createElements(TPointsAroundCenter.class);
    proposed_targets.points[2].x = 1;
    proposed_targets.points[2] = point(3, 2);
    proposed_targets.central_point = point(2, 2);
    println(">>> Nested complex structures with arrays of complex types: OK");

    TAlienRobot abot2 = new TAlienRobot();
    createElements(abot2);            //here abot2 is not null so we can analyse its type and allocate memory if needed
    abot2.visited_places[1].x = 0;
    abot2.bot_structure.points[1].y = 2;
    if (abot2.name.length() > 0)
        println("Alien bot2 name is not empty!");   //abot2.name intentionally not set - test that length method is working
    else println("Alien bot2 name is empty");
    println(abot2.access_passwords[2]);
    abot2.access_passwords[2] = "Xurpta";
    println(abot2.access_passwords[2]);

    //createElements(abot);  //this will not work because abot==null (and type can not be read from null)
    abot = createElements(TAlienRobot.class);    //so the class should be explicitly given to createElements()
    abot.danger_zones[1].points[3].y = 3;
    abot.targets[2] = point(4, 4);
    abot.name = "Destroyer";
    if (abot.name.length() > 0) println("Alien bot name is not empty!");
    else println("Alien bot name is empty!");
    println(">>> Complex structures with strings: OK");

    String[] known_alien_names = new String[10];
    createElements(known_alien_names);
    if (known_alien_names[0].length() > 0) println(known_alien_names[0]);
    else println("Alien name is empty");
    known_alien_names[0] = "Blurp";
    known_alien_names[1] = "Slurp";
    known_alien_names[2] = "Plurp";
    println(known_alien_names[0]);
    println(">>> Local array of strings: OK");

    abot.sub_bot.name = "Bot of second level";
    abot.sub_bot.sub_bot.name = "Bot of third level";
    abot.sub_bot.sub_bot.sub_bot.name = "Bot of fourth level";
    println(abot.sub_bot.name);
    println(abot.sub_bot.sub_bot.name);
    println(abot.sub_bot.sub_bot.sub_bot.name);
    println(">>> Recurrent substructures:  OK");
}


//************************************************************************
//                             Example B.
//           A little more reasonable hierarchy of types
//                          (The University)
//************************************************************************

public static class TAddress {
    String country;
    String city;
    String street;
    String house_number;
    String flat_number;
}

public enum TSex {    //enum type, static by default
    male, female, unknown
}

public static class TPerson {
    String personID;   //unique identifier
    String name;
    String surname;
    int age;
    int weight;
    TSex sex;
    TAddress home_address;
}

final static int MAX_GRADES = 100;
final static int MAX_PERSONS = 1000;

public static class TStudent {      //very simplified student type
    TPerson personal_data;
    double[] grades = new double[MAX_GRADES];
    int gradeCount = 0;
}

public static class TTeacher {      //very simplified teacher type
    TPerson personal_data;
    int salary;
}

public static class TUniversity {   //very simplified university structure
    String name;
    TAddress address;
    TStudent[] students = new TStudent[MAX_PERSONS];
    TTeacher[] teachers = new TTeacher[MAX_PERSONS];
    int studentCount = 0;
    int teacherCount = 0;
    int yearly_income;
}

//************************************************************************
//                             Example B.
//       Procedures to create/use the University data to structures
//    
//************************************************************************

public static void setAddress(TAddress a, String country, String city, String street, String house, String flat) {
    a.country = country;
    a.city = city;
    a.street = street;
    a.house_number = house;
    a.flat_number = flat;
}

public static void setPerson(TPerson p, String id, String name, String surname, int age, int weight, TSex sex, TAddress addr) {
    p.personID = id;
    p.name = name;
    p.surname = surname;
    p.age = age;
    p.weight = weight;
    p.sex = sex;
    p.home_address = addr;
}

public static void setStudent(TStudent s, TPerson person, double... grades) {
    s.personal_data = person;
    s.gradeCount = 0;
    for (double g : grades) addGrade(s, g);  //grades is a set of double entries through vararg
}

public static void setTeacher(TTeacher t, TPerson person, int salary) {
    t.personal_data = person;
    t.salary = salary;
}

public static void setUniversity(TUniversity u, String name, TAddress addr, int yearly_income) {
    u.name = name;
    u.address = addr;
    u.yearly_income = yearly_income;
}

public static TAddress createAddress(String country, String city, String street, String house, String flat) {
    TAddress a = new TAddress();
    setAddress(a, country, city, street, house, flat);
    return a;
}

public static TAddress createAddress(String country, String city) {
    return createAddress(country, city, "", "", "");
}


public static TPerson createPerson(String id, String name, String surname, int age, int weight, TSex sex, TAddress addr) {
    TPerson p = new TPerson();
    setPerson(p, id, name, surname, age, weight, sex, addr);
    return p;
}

public static TPerson createPerson(String name, String surname) {
    return createPerson("", name, surname, 0, 0, TSex.unknown,
            createAddress("Unknown", "Unknown", "Unknown", "Unknown", "Unknown"));
}

public static TStudent createStudent(TPerson person, double... grades) {
    TStudent s = new TStudent();
    setStudent(s, person, grades);
    return s;
}

public static TTeacher createTeacher(TPerson person, int salary) {
    TTeacher t = new TTeacher();
    setTeacher(t, person, salary);
    return t;
}

public static void addStudent(TUniversity uni, TStudent s) {
    if (uni.studentCount < MAX_PERSONS)
        uni.students[uni.studentCount++] = s;
}

public static void addTeacher(TUniversity uni, TTeacher t) {
    if (uni.teacherCount < MAX_PERSONS)
        uni.teachers[uni.teacherCount++] = t;
}

public static void addGrade(TStudent s, double grade) {
    if (s.gradeCount < MAX_GRADES)
        s.grades[s.gradeCount++] = grade;
}

// Calculating average of student' grades
public static double calculateStudGradesAverage(TStudent s) {
    if (s.gradeCount == 0) return 0;
    int sum = 0;
    for (int i = 0; i < s.gradeCount; i++) sum += (int) s.grades[i];
    return (double) sum / s.gradeCount;
}

public static TStudent findBestStudent(TUniversity uni) {
    if (uni.studentCount == 0) return null;
    TStudent best = uni.students[0];
    double bestAvg = calculateStudGradesAverage(best);
    for (int i = 1; i < uni.studentCount; i++) {
        double avg = calculateStudGradesAverage(uni.students[i]);
        if (avg > bestAvg) {
            best = uni.students[i];
            bestAvg = avg;
        }
    }
    return best;
}

public static int findStudentIndexByName(TUniversity uni, String name) {
    for (int i = 0; i < uni.studentCount; i++) {
        if (uni.students[i].personal_data.name.equalsIgnoreCase(name)) {
            return i;
        }
    }
    return -1; //no match
}

public static TStudent findStudentByName(TUniversity uni, String name) {
    int index_id = findStudentIndexByName(uni, name);
    if (index_id >= 0) return uni.students[index_id];
    else return null; //no match
}

public static boolean isUnivIncomeHigherThanTeachersSalary(TUniversity uni) {
    int totalSalaries = 0;
    for (int i = 0; i < uni.teacherCount; i++) {
        totalSalaries += uni.teachers[i].salary;
    }
    return uni.yearly_income > totalSalaries;
}

public static void printAddress(TAddress a) {
    System.out.println("Address: " + a.street + " " + a.house_number + "/" + a.flat_number + ", " + a.city + ", " + a.country);
}

public static void printPerson(TPerson p) {
    println(p.name + " " + p.surname + " (" + p.sex + "), age: " + p.age + ", weight: " + p.weight);
    printAddress(p.home_address);
}

public static void printStudent(TStudent s) {
    printPerson(s.personal_data);
    System.out.printf("Average grade: %.2f%n", calculateStudGradesAverage(s));
}

public static void printTeacher(TTeacher t) {
    printPerson(t.personal_data);
    println("Salary: " + t.salary);
}

public static void printStudents(TUniversity uni) {
    System.out.println("--- Students ---");
    for (int i = 0; i < uni.studentCount; i++) {
        printStudent(uni.students[i]);
        println("--");
    }
}

public static void printTeachers(TUniversity uni) {
    System.out.println("--- Teachers ---");
    for (int i = 0; i < uni.teacherCount; i++) {
        printTeacher(uni.teachers[i]);
        println("--");
    }
}

public static void printUniversity(TUniversity uni) {
    System.out.println("=== UNIVERSITY: " + uni.name + " ===");
    System.out.print("Univ Address: ");
    printAddress(uni.address);
    System.out.println("Yearly income: " + uni.yearly_income + "\n");

    printStudents(uni);
    printTeachers(uni);
    println("================");
}

//public static void addExampleUniversityData(TUniversity univ) {
//    TStudent s = createStudent(createPerson("Sarah", "Connor"), 4, 3, 5, 4.5);
//    addStudent(univ, s);
//    addStudent(univ, createStudent(createPerson("Kyle", "Reese"), 3, 3.5, 5.5, 4, 4.5));  //objects are created just to pass the data as variables
//    addStudent(univ, createStudent(createPerson("Peter", "Silberman"), 4, 4.5));
//    addStudent(univ, createStudent(createPerson("T800", "Cyberdyne"), 7, 7, 7, 7, 7));
//
//    setUniversity(univ, "Boston University", createAddress("USA", "Boston"), 1234567);
//    univ.students[0].personal_data.sex = TSex.female;  //manual patching - not recommended but available
//    univ.students[1].personal_data.sex = TSex.male;    //manual patching - not recommended but available
//    univ.students[2].personal_data.sex = TSex.male;    //manual patching - not recommended but available
//    univ.students[findStudentIndexByName(univ, "T800")].personal_data.weight = 180;  //very risky: if student not found => error
//    //findStudentByName(univ1,"T800").personal_data.weight=180;   //also risky solution - NullPointerException if student not found
//}

// 1. Declaration of students
public static void addUniversityData(TUniversity univ) {
    setUniversity(univ, "Politechnika Wrocławska",
            createAddress("Poland", "Wrocław", "Wybrzeże Wyspiańskiego", "27", "1"), (int) 5E8);

    TStudent s1 = createStudent(
            createPerson("SC1", "Sarah", "Connor", 25, 65, TSex.female,
                    createAddress("Poland", "Warszawa", "Aleje Ujazdowskie", "8", "5")),
            4.0, 3.0, 5.0, 4.5);
    addStudent(univ, s1);

    TStudent s2 = createStudent(
            createPerson("KR2", "Kyle", "Reese", 21, 80, TSex.male,
                    createAddress("Poland", "Kraków", "Długa", "15", "1")),
            3.0, 3.5, 5.5, 4.0, 4.5);
    addStudent(univ, s2);

    TStudent s3 = createStudent(
            createPerson("PS3", "Peter", "Silberman", 23, 75, TSex.male,
                    createAddress("Poland", "Warszawa", "Prosta", "2", "11")),
            4.0, 4.5);
    addStudent(univ, s3);

    TStudent s4 = createStudent(
            createPerson("T84", "T800", "Cyberdyne", 800, 180, TSex.unknown,
                    createAddress("Poland", "Gdańsk", "Terminatora", "800", "1")),
            7.0, 7.0, 7.0, 7.0, 7.0);
    addStudent(univ, s4);

    TStudent s5 = createStudent(
            createPerson("GN5", "Grzegorz", "Nowak", 19, 92, TSex.male,
                    createAddress("Poland", "Kraków", "Słowackiego", "3", "3A")),
            2.0, 4.0, 5.0, 4.5, 5.0, 5.0, 5.0);
    addStudent(univ, s5);

    TStudent s6 = createStudent(
            createPerson("JK6", "Jan", "Kowalski", 20, 70, TSex.male,
                    createAddress("Poland", "Kraków", "Rynek Główny", "1", "1")),
            4.0, 5.0, 6.0, 1.0, 2.0);
    addStudent(univ, s6);

    TStudent s7 = createStudent(
            createPerson("KN7", "Krystian", "Nowak", 22, 68, TSex.male,
                    createAddress("Poland", "Gdańsk", "Długi Targ", "2", "2")),
            4.0, 0.0, 2.0, 2.0, 2.0);
    addStudent(univ, s7);

    TStudent s8 = createStudent(
            createPerson("LM8", "Ludmiła", "Miła", 24, 55, TSex.female,
                    createAddress("Poland", "Poznań", "Święty Marcin", "10", "10B")),
            3.0, 3.0, 3.0, 3.0, 3.0, 5.0);
    addStudent(univ, s8);

    TStudent s9 = createStudent(
            createPerson("BM9", "Bogumił", "Miły", 25, 72, TSex.male,
                    createAddress("Poland", "Poznań", "Wrocławska", "50", "2")),
            2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0);
    addStudent(univ, s9);

    TStudent s10 = createStudent(
            createPerson("KJ10", "Krystyna", "Janda", 55, 60, TSex.female,
                    createAddress("Poland", "Warszawa", "Marszałkowska", "1", "1A")),
            5.5, 4.5, 3.5, 2.5, 1.5);
    addStudent(univ, s10);

    TStudent s11 = createStudent(
            createPerson("KD11", "Krzesimir", "Dębski", 60, 78, TSex.male,
                    createAddress("Poland", "Gdańsk", "Oliwska", "30", "3")),
            4.0, 4.0, 4.0, 4.5);
    addStudent(univ, s11);

    TStudent s12 = createStudent(
            createPerson("PM12", "Piotr", "Mróz", 26, 85, TSex.male,
                    createAddress("Poland", "Wrocław", "Grunwaldzka", "1", "1")),
            3.0, 5.0, 6.0, 7.0, 0.0, 1.0);
    addStudent(univ, s12);

    TStudent s13 = createStudent(
            createPerson("PD13", "Paweł", "Dacewicz", 20, 71, TSex.male,
                    createAddress("Poland", "Wrocław", "Powstańców Śląskich", "10", "3")),
            3.0, 4.0, 4.0, 5.0, 2.0, 5.0);
    addStudent(univ, s13);
}


// 2. Collecting students of given sex in an array
static class TStudentContainer {
    TStudent[] AllStudents;
    TStudent[] MaleStudents;
    TStudent[] FemaleStudents;
    TStudent[] UnknownStudents;

    public TStudentContainer(TStudent[] allStudents, TStudent[] maleStudents, TStudent[] femaleStudents, TStudent[] unknownStudents) {
        AllStudents = allStudents;
        MaleStudents = maleStudents;
        FemaleStudents = femaleStudents;
        UnknownStudents = unknownStudents;
    }
}

TStudentContainer GroupStudents(TUniversity univ) {
    TStudent[] AllStudents = new TStudent[MAX_PERSONS];
    TStudent[] MaleStudents = new TStudent[MAX_PERSONS];
    TStudent[] FemaleStudents = new TStudent[MAX_PERSONS];
    TStudent[] UnknownStudents = new TStudent[MAX_PERSONS];
    int males = 0, females = 0, unknowns = 0;

    TStudent[] Students = univ.students;
    for (int i = 0; i < univ.studentCount; i++) {
        TStudent student = Students[i];
        if (student.personal_data.sex == TSex.male) {
            MaleStudents[males] = student;
            AllStudents[males + females + unknowns] = student;
            males += 1;
        } else if (student.personal_data.sex == TSex.female) {
            FemaleStudents[females] = student;
            AllStudents[males + females + unknowns] = student;
            females += 1;
        } else if (student.personal_data.sex == TSex.unknown) {
            UnknownStudents[unknowns] = student;
            AllStudents[males + females + unknowns] = student;
            unknowns += 1;
        }
    }

    TStudent[] AllStudents2 = new TStudent[males + females + unknowns];
    TStudent[] MaleStudents2 = new TStudent[males];
    TStudent[] FemaleStudents2 = new TStudent[females];
    TStudent[] UnknownStudents2 = new TStudent[unknowns];
    for (int i = 0; i <= MAX_PERSONS - 1; i++) {
        if (i <= males + females + unknowns - 1) {
            AllStudents2[i] = AllStudents[i];
        }
        if (i <= males - 1) {
            MaleStudents2[i] = MaleStudents[i];
        }
        if (i <= females - 1) {
            FemaleStudents2[i] = FemaleStudents[i];
        }
        if (i <= unknowns - 1) {
            UnknownStudents2[i] = UnknownStudents[i];
        }
    }
    return new TStudentContainer(AllStudents2, MaleStudents2, FemaleStudents2, UnknownStudents2);
}

// 3. Display the students by sex
public enum TDisplayCriteria {
    SEX,
    ALL
}

void DisplayStudents(TStudentContainer sbs, TDisplayCriteria criterion) {
    if (criterion == TDisplayCriteria.SEX) {
        TStudent[] MaleStudents = sbs.MaleStudents;
        println("=== Male students ===");
        for (TStudent student : MaleStudents) {
            printStudent(student);
        }
        TStudent[] FemaleStudents = sbs.FemaleStudents;
        println("=== Female students ===");
        for (TStudent student : FemaleStudents) {
            printStudent(student);
        }
        TStudent[] UnknownStudents = sbs.UnknownStudents;
        println("=== Unknown students ===");
        for (TStudent student : UnknownStudents) {
            printStudent(student);
        }
    }
    else {
        TStudent[] AllStudents = sbs.AllStudents;
        println("=== All students ===");
        for (TStudent student : AllStudents) {
            printStudent(student);
        }
    }
    println();
}

// 4. Sorting
public enum TSortCriteria {
    SURNAME,
    WEIGHT,
    ADDRESS,
    CITY
}

private static int compareStudents(TStudent s1, TStudent s2, TSortCriteria criterion) {
    return switch (criterion) {
        case SURNAME -> s1.personal_data.surname.compareTo(s2.personal_data.surname);
        case WEIGHT -> s1.personal_data.weight - s2.personal_data.weight;
        case ADDRESS -> s1.personal_data.home_address.street.compareTo(s2.personal_data.home_address.street);
        case CITY -> s1.personal_data.home_address.city.compareTo(s2.personal_data.home_address.city);
    };
}

public static TStudent[] sortStudents(TStudentContainer container, TSortCriteria criterion) {
    TStudent[] students = container.AllStudents;
    int n = students.length;
    boolean swapped;
    for (int i = 0; i < n - 1; i++) {
        swapped = false;
        for (int j = 0; j < n - 1 - i; j++) {
            if (compareStudents(students[j], students[j + 1], criterion) > 0) {
                TStudent temp = students[j];
                students[j] = students[j + 1];
                students[j + 1] = temp;
                swapped = true;
            }
        }
        if (!swapped) break;
    }
    return students;
}

// 5. Moving the students to another university
/** Returns true if two addresses are equal, false otherwise */
private static boolean TAddressEquals(TAddress a1, TAddress a2) {
    if (a1 == null && a2 == null) return true;
    if (a1 == null || a2 == null) return false;

    return a1.country.equals(a2.country) &&
            a1.city.equals(a2.city) &&
            a1.street.equals(a2.street) &&
            a1.house_number.equals(a2.house_number) &&
            a1.flat_number.equals(a2.flat_number);
}

/** PersonalMatch: Returns true if ALL persons' attributes are equal, false otherwise. 
 * AddressMatch: Returns true if two addresses are equal, false otherwise
 */
private static boolean TPersonEquals(TPerson p1, TPerson p2) {
    if (p1 == null && p2 == null) return true;
    if (p1 == null || p2 == null) return false;

    boolean personalMatch = p1.name.equals(p2.name) &&
            p1.surname.equals(p2.surname) &&
            p1.age == p2.age &&
            p1.weight == p2.weight &&
            p1.sex == p2.sex;

    boolean addressMatch = TAddressEquals(p1.home_address, p2.home_address);
    return personalMatch && addressMatch;
}

/** Returns true if students are not duplicates, false otherwise */
boolean StudentCheck(TStudent p1, TStudent p2) {
    TPerson Person1 = p1.personal_data;
    TPerson Person2 = p2.personal_data;
    return !TPersonEquals(Person1, Person2);
}

public void TransferStudents(TUniversity uni1, TUniversity uni2) {
    TStudent[] Students1 = GroupStudents(uni1).AllStudents;
    TStudent[] Students2 = GroupStudents(uni2).AllStudents;
    for (int i = 0; i <= Students1.length - 1; i++) {
        int duplicateCount = Students2.length;
        for (int j = 0; j <= Students2.length - 1; j++) {
            if (StudentCheck(Students1[i], Students2[j])) {
                duplicateCount -= 1;
            }
            if (duplicateCount == 0) {
                addStudent(uni2, Students1[i]);
            }
        }
    }
}

void main() {
    //Example A: tests of data creation/access
    clrscr();
    StructInitTest();
    print("Press any key to continue...");
    wait_for_any_key();

    //Example B: the University
    clrscr();
    TUniversity univ1 = createElements(TUniversity.class);  //"magic" line to allocate memory of the whole structure
    addUniversityData(univ1);

    println("\n1. Displaying the initialized university");
    printUniversity(univ1);
    println();

    println("\n3. Students displayed by sex");
    TStudentContainer GroupedStudents = GroupStudents(univ1);
    DisplayStudents(GroupedStudents, TDisplayCriteria.SEX);

    final TSortCriteria Criterion = TSortCriteria.CITY;
    println("\n4. Sorting the list of students by the criterion " + Criterion);
    println("\nList of students before sorting:");
    DisplayStudents(GroupedStudents, TDisplayCriteria.ALL);

    TStudent[] SortedStudents = sortStudents(GroupedStudents, Criterion);
    println("\nList of students after sorting:");
    for (TStudent student : SortedStudents) {
        printStudent(student);
    }
    
    println("\n5. Adding students to another university");
    TUniversity univ2 = createElements(TUniversity.class);
    setUniversity(univ2, "Akademia Górniczo-Hutnicza", createAddress("Poland", "Kraków"), (int) 7E8);
    // For demonstration purposes, a duplicate student should be added (e.g. student 12)
    TStudent s12 = createStudent(
            createPerson("PM12", "Piotr", "Mróz", 26, 85, TSex.male,
                    createAddress("Poland", "Wrocław", "Grunwaldzka", "1", "1")),
            3.0, 5.0, 6.0, 7.0, 0.0, 1.0);
    addStudent(univ2, s12);
    println("\nBefore transfer:");
    DisplayStudents(GroupStudents(univ2), TDisplayCriteria.ALL);
    TransferStudents(univ1, univ2);
    println("\nAfter transfer:");
    DisplayStudents(GroupStudents(univ2), TDisplayCriteria.ALL);

    println("=== Best Student ===");
    printStudent(findBestStudent(univ1));
    println("===============");
}