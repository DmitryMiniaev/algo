        int[] a = new int[] {6, 4, 16, 10, 16, 14, 2, 8};
        int tlen = (Integer.highestOneBit(a.length) << 1) - 1;
        int[] sum = new int[tlen];
        int lvlLo = (tlen - tlen / 2 - 1);
        int lvlHi = tlen;
        for (int i = lvlLo; i < lvlHi; i++) {
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
        System.out.println(Arrays.toString(sum));
        System.out.println(Arrays.toString(fromLeft));
        System.out.println(Arrays.toString(ps));
