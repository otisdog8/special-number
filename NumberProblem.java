import java.util.LinkedList;

class NumberProblem {
    public static void main(String[] args) {
        // Rules:
        // 1 [1,3,7,9]
        // 2 [2,4,6,8]
        // 3 [1,3,7,9] such that sum of all preceding things mod 3 = 0
        // 4 [2,4,6,8] last two digits such that mod 4 = 0
        // 5 [5]
        // 6 [2,4,6,8] such that the sum of all preceding things mod 3 = 0
        // 7 [1,3,7,9]
        // 8 [2,4,6,8] last three digits such that mod 8 = 0
        // 9 [1,3,7,9] such that the sum of all preceding things mod 9 = 0
        // 10 [0]
        int[] odds = new int[] { 1, 3, 7, 9 };
        int[] evens = new int[] { 2, 4, 6, 8 };

        // Chains 1,2,3 3,4 4,5,6 6,7,8 9,10

        // Generate chain 1
        LinkedList<Chain> firstChain = new LinkedList<Chain>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if ((odds[i] + evens[j] + odds[k]) % 3 == 0 && i != k) {
                        int[] chain1 = new int[3];
                        chain1[0] = odds[i];
                        chain1[1] = evens[j];
                        chain1[2] = odds[k];
                        firstChain.add(new Chain(chain1));
                    }
                }
            }
        }

        // Generate chain 2
        LinkedList<Chain> secondChain = new LinkedList<Chain>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((odds[i] * 10 + evens[j]) % 4 == 0) {
                    int[] chain2 = new int[2];
                    chain2[0] = odds[i];
                    chain2[1] = evens[j];
                    secondChain.add(new Chain(chain2));
                }
            }
        }

        // Generate chain 3
        LinkedList<Chain> thirdChain = new LinkedList<Chain>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((evens[i] + 5 + evens[j]) % 3 == 0 && i != j) {
                    int[] chain3 = new int[3];
                    chain3[0] = evens[i];
                    chain3[1] = 5;
                    chain3[2] = evens[j];
                    thirdChain.add(new Chain(chain3));
                }
            }
        }

        // Generate chain 4
        LinkedList<Chain> fourthChain = new LinkedList<Chain>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    if ((evens[i] * 100 + odds[j] * 10 + evens[k]) % 8 == 0 && i != k) {
                        int[] chain4 = new int[3];
                        chain4[0] = evens[i];
                        chain4[1] = odds[j];
                        chain4[2] = evens[k];
                        fourthChain.add(new Chain(chain4));
                    }
                }

            }
        }

        // Generate chain 5
        LinkedList<Chain> fifthChain = new LinkedList<Chain>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int[] chain5 = new int[3];
                chain5[0] = evens[i];
                chain5[1] = odds[j];
                chain5[2] = 0;
                fifthChain.add(new Chain(chain5));
            }
        }

        // Chain them up
        LinkedList<Chain> completedChains = new LinkedList<Chain>();
        Chain tempChain1;
        Chain tempChain2;
        Chain tempChain3;

        // If the chains can connect, branch and start trying the next level of chains,
        // and repeat until all the chains are done.
        for (int i = 0; i < firstChain.size(); i++) {
            for (int j = 0; j < secondChain.size(); j++) {
                if (firstChain.get(i).check(secondChain.get(j))) {
                    tempChain1 = firstChain.get(i).copy();
                    tempChain1.add(secondChain.get(j));

                    for (int k = 0; k < thirdChain.size(); k++) {

                        if (tempChain1.check(thirdChain.get(k))) {
                            tempChain2 = tempChain1.copy();
                            tempChain2.add(thirdChain.get(k));

                            for (int l = 0; l < fourthChain.size(); l++) {
                                if (tempChain2.check(fourthChain.get(l))) {
                                    tempChain3 = tempChain2.copy();
                                    tempChain3.add(fourthChain.get(l));
                                    for (int m = 0; m < fifthChain.size(); m++) {
                                        if (tempChain3.check(fifthChain.get(m))) {
                                            tempChain3.add(fifthChain.get(m));
                                            completedChains.add(tempChain3);
                                        }
                                    }

                                }
                            }

                        }
                    }

                }
            }
        }
        print("Number of guesses (worst case):  " + completedChains.size());

        // Check all the guesses
        for (int i = 0; i < completedChains.size(); i++) {
            if (checkvalidity(completedChains.get(i))) {
            }
        }
    }

    private static boolean checkvalidity(Chain chain) {
        long placeholdnum = 0L;

        for (int i = 1; i < 11; i++) {
            placeholdnum += chain.getvalues()[i - 1];

            if (placeholdnum % i != 0) {
                return false;
            }

            placeholdnum *= 10;
        }

        print("Final number:   " + placeholdnum / 10);

        return true;
    }

    private static void print(Object thing) {
        System.out.println(thing);
    }

}

class Chain {
    int[] values;

    Chain(int[] values) {
        this.values = values;
    }

    public boolean conflicts(Chain otherChain) {
        boolean result = false;

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < otherChain.getvalues().length; j++) {
                if (otherChain.getvalues()[j] == values[i] && (i != values.length - 1 && j != 0)) {
                    result = true;
                }
            }
        }

        return result;
    }

    public boolean compatible(Chain otherChain) {
        return otherChain.getvalues()[0] == this.values[values.length - 1];
    }

    public boolean check(Chain otherChain) {
        return !conflicts(otherChain) && compatible(otherChain);
    }

    public Chain copy() {
        return new Chain(values);
    }

    public void add(Chain otherChain) {
        int[] otherValues = otherChain.getvalues();
        int[] newValues = new int[values.length + otherValues.length - 1];

        for (int i = 0; i < values.length; i++) {
            newValues[i] = values[i];
        }

        for (int i = 1; i < otherValues.length; i++) {
            newValues[i + values.length - 1] = otherValues[i];
        }

        this.values = newValues;
    }

    public int[] getvalues() {
        return values;
    }
}