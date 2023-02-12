public static void main(String[] args) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    //sorts
    int[][] a = {{2, 1},{1, 3},{3, 2}};
    // sorts a by first element
    Arrays.sort(a, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });
    Arrays.sort(a, Comparator.comparingInt(o -> o[0]));
    PriorityQueue<int[]> q = new PriorityQueue<>(Comparator.comparingInt(o->o[0]));
    //dates
    LocalDate date = LocalDate.of(1901, 1, 1); // January 1st
    date = date.plusMonths(1);
    date.getDayOfWeek() == DayOfWeek.SUNDAY;
    
    br.close();
    bw.close();
}