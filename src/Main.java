
void main() {
    menuOne();
}

void menuOne() {
    Scanner scanner = new Scanner(System.in);
    byte optionMenuOne;
    do {
        IO.println("Please, select an option from bellow: ");
        IO.println("1. Exercise One.");
        IO.println("2. Exercise Two.");
        IO.println("3. Exercise Three.");
        IO.println("0. Exit.");
        IO.print("Select:  ");
        optionMenuOne = scanner.nextByte();
        switch (optionMenuOne) {
            case 1:
                exerciseOne();
                break;
            case 2:
                exerciseTwo();
                break;
            case 3:
                exerciseThree();
                break;
            case 0:
                IO.println("Thank you for your time! ");
                break;
            default:
                IO.println("Wrong Output, please try again.");
        }
    } while (optionMenuOne != 0);
}

void exerciseOne() {
    Random random = new Random();
    List<Integer> prices = new ArrayList<>();
    for (int i = 0; i < random.nextInt(25) + 1; i++) {
        prices.add(random.nextInt(100) + 1);
    }
    List<Integer> top3Prices = getTop3Prices(prices);
    IO.println("Exercise One Result: ");
    IO.println("All prices: " + prices);
    IO.println("3 highest prices: " + top3Prices);
}

void exerciseTwo() {
    Random random = new Random();

    int n = random.nextInt(20) + 1;
    List<Long> prices = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        prices.add((long) (random.nextInt(100000) + 1000));
    }

    String[] coupons = {"NONE", "DESC10", "DESC20", "FREESHIP"};
    String coupon = coupons[random.nextInt(coupons.length)];

    long shipping = random.nextInt(50000) + 5000;

    calculateInvoice(prices, coupon, shipping);
}

void exerciseThree() {
    Random random = new Random();

    // Random Measures
    int n = random.nextInt(20) + 5;
    List<Integer> measurements = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        measurements.add(random.nextInt(200) + 10);
    }

    // Invert array
    List<Integer> inverted = invertArray(measurements);

    // Calculate statistics
    long total = calculateTotal(measurements);
    long average = calculateAverage(measurements);
    int max = findMax(measurements);
    int min = findMin(measurements);

    // Detect peaks
    List<Integer> peakIndices = detectPeaks(measurements);

    // Find strongest peak
    int strongestPeakIndex = findStrongestPeak(measurements, peakIndices);

    // Print results
    IO.println("Exercise Three Result:");
    IO.println("INV: " + inverted);
    IO.println("TOTAL: " + total);
    IO.println("PROMEDIO: " + average);
    IO.println("MAX: " + max);
    IO.println("MIN: " + min);
    IO.println("PICOS: " + peakIndices.size());

    if (peakIndices.isEmpty()) {
        IO.println("INDICES_PICOS: NONE");
    } else {
        IO.println("INDICES_PICOS: " + peakIndices);
    }

    if (strongestPeakIndex == -1) {
        IO.println("PICO_MAS_FUERTE: -1");
    } else {
        IO.println("PICO_MAS_FUERTE: " + strongestPeakIndex + " (valor: " + measurements.get(strongestPeakIndex) + ")");
    }
}

List<Integer> getTop3Prices(List<Integer> prices) {
    List<Integer> top3 = new ArrayList<>();
    for (int price : prices) {
        insertInTop3(top3, price);
    }
    return top3;
}

void insertInTop3(List<Integer> top3, int price) {
    if (top3.size() < 3) {
        int insertPos = 0;
        for (int i = 0; i < top3.size(); i++) {
            if (price > top3.get(i)) {
                insertPos = i;
                break;
            }
            insertPos = i + 1;
        }
        top3.add(insertPos, price);
    } else {
        if (price > top3.get(2)) {
            int insertPos = 2;
            for (int i = 0; i < 3; i++) {
                if (price > top3.get(i)) {
                    insertPos = i;
                    break;
                }
            }
            top3.add(insertPos, price);
            top3.remove(3);
        }
    }
}

void calculateInvoice(List<Long> prices, String coupon, long envio) {

    long subtotal = 0;
    for (long price : prices) {
        subtotal += price;
    }

    long descuento = 0;
    long envioAplicado = envio;

    switch (coupon) {
        case "DESC10":
            descuento = subtotal / 10;
            break;
        case "DESC20":
            if (subtotal >= 200000) {
                descuento = (subtotal * 20) / 100;
            }
            break;
        case "FREESHIP":
            envioAplicado = 0;
            break;
        case "NONE":
            break;
    }

    long base = subtotal - descuento;
    long iva = Math.round((base * 19.0) / 100.0);
    long total = base + iva + envioAplicado;

    // Format the prices to COP (Just Because)
    java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");

    IO.println("Exercise Two Result:");
    IO.println("SUBTOTAL $" + df.format(subtotal) + " COP");
    IO.println("DESCUENTO $" + df.format(descuento) + " COP");
    IO.println("IVA $" + df.format(iva) + " COP");
    IO.println("TOTAL $" + df.format(total) + " COP");
    IO.println("Coupon applied: " + coupon);
}

List<Integer> invertArray(List<Integer> arr) {
    List<Integer> inverted = new ArrayList<>();
    for (int i = arr.size() - 1; i >= 0; i--) {
        inverted.add(arr.get(i));
    }
    return inverted;
}

long calculateTotal(List<Integer> measurements) {
    long total = 0;
    for (int m : measurements) {
        total += m;
    }
    return total;
}

long calculateAverage(List<Integer> measurements) {
    if (measurements.isEmpty()) return 0;
    long total = calculateTotal(measurements);
    return Math.round((double) total / measurements.size());
}

int findMax(List<Integer> measurements) {
    int max = measurements.getFirst();
    for (int m : measurements) {
        if (m > max) {
            max = m;
        }
    }
    return max;
}

int findMin(List<Integer> measurements) {
    int min = measurements.getFirst();
    for (int m : measurements) {
        if (m < min) {
            min = m;
        }
    }
    return min;
}

List<Integer> detectPeaks(List<Integer> measurements) {
    List<Integer> peakIndices = new ArrayList<>();

    // Pico es: 0 < i < n-1 and m[i] > m[i-1] and m[i] > m[i+1]
    for (int i = 1; i < measurements.size() - 1; i++) {
        if (measurements.get(i) > measurements.get(i - 1) &&
                measurements.get(i) > measurements.get(i + 1)) {
            peakIndices.add(i);
        }
    }

    return peakIndices;
}

int findStrongestPeak(List<Integer> measurements, List<Integer> peakIndices) {
    if (peakIndices.isEmpty()) {
        return -1;
    }

    int strongestIndex = peakIndices.getFirst();
    int maxValue = measurements.get(strongestIndex);

    for (int idx : peakIndices) {
        int value = measurements.get(idx);
        if (value > maxValue) {
            maxValue = value;
            strongestIndex = idx;
        }
    }

    return strongestIndex;
}


