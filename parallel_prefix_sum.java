        int[] a = new int[10000];
        int[] b = new int[10000];
        for (int i = 0; i < a.length; i++) {
            a[i] = rnd.nextInt();
            b[i] = a[i];
        }
//        int[] a = new int[] {6, 4, 16, 10, 16, 14, 2, 8, 1};
//        int[] b = new int[] {6, 4, 16, 10, 16, 14, 2, 8, 1};
        int tlen = 1;
        while (a.length > Integer.highestOneBit(tlen)) {
            tlen <<= 1;
            tlen |= 1;
        }
        int[] sum = new int[tlen];
        int lvlLo = (tlen - tlen / 2 - 1);
        int lvlHi = tlen;
        for (int i = lvlLo; i - lvlLo < a.length ; i++) {
            sum[i] = a[i - lvlLo];
        }
        int lvlCount = Integer.bitCount(tlen) - 1;
        for(int i = 0; i < lvlCount; i++) {
            for (int j = lvlLo; j < lvlHi; j+=2) {
                sum[j / 2] = sum[j] + sum[j + 1];
            }
            lvlHi = lvlLo;
            lvlLo = lvlLo - lvlLo / 2 - 1;
        }
        lvlLo = 0;
        lvlHi = 1;
        int[] fromLeft = new int[tlen];
        for (int i = 0; i < lvlCount; i++) {
            for (int j = lvlLo; j < lvlHi; j++) {
                fromLeft[j * 2 + 2] = fromLeft[j] + sum[j * 2 + 1];
                fromLeft[j * 2 + 1] = fromLeft[j];
            }
            lvlLo = lvlHi;
            lvlHi = lvlHi * 2 + 1;
        }
        int offset = (tlen - tlen / 2 - 1);;
        int[] ps = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            ps[i] = a[i] + fromLeft[offset + i];
        }

        int[] ps2 = new int[a.length];
        ps2[0] = b[0];
        for (int i = 1; i < a.length; i++) {
            ps2[i] = ps2[i - 1] + b[i];
        }
//        System.out.println(Arrays.toString(ps));
//        System.out.println(Arrays.toString(ps2));
        for (int i = 0; i < a.length; i++) {
            if(ps[i] != ps2[i]) {
                throw new RuntimeException();
            }
        }
