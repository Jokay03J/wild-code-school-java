class Classroom {

    public static void main(String[] args) {
        Wilder firstWilder = new Wilder("Jean-Claude", true);
        Wilder secondWilder = new Wilder("Henri", false);
        System.out.println(firstWilder.whoAmI());
        System.out.println(secondWilder.whoAmI());
    }
}
