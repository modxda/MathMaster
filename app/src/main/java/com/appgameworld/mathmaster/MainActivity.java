package com.appgameworld.mathmaster;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity {
    private final int navy = Color.rgb(24, 43, 98);
    private final int blue = Color.rgb(49, 87, 213);
    private final int pale = Color.rgb(241, 245, 255);
    private LinearLayout page;
    private final Random random = new Random();

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        showHome();
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(0, 6, 0, 6);
        return t;
    }

    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(20, 16, 20, 24);
        return l;
    }

    private void base(String title, String subtitle) {
        ScrollView scroll = new ScrollView(this);
        page = column();
        page.setBackgroundColor(Color.WHITE);
        TextView h = text(title, 27, navy, true);
        page.addView(h);
        page.addView(text(subtitle, 15, Color.DKGRAY, false));
        View line = new View(this); line.setBackgroundColor(blue);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, 3);
        lp.setMargins(0, 12, 0, 16); page.addView(line, lp);
        scroll.addView(page);
        setContentView(scroll);
    }

    private Button button(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label); b.setTextColor(Color.WHITE); b.setAllCaps(false);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(blue));
        b.setOnClickListener(v -> action.run());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 7, 0, 7); page.addView(b, lp);
        return b;
    }

    private EditText field(String hint) {
        EditText e = new EditText(this);
        e.setSingleLine(true); e.setTextSize(16); e.setHint(hint);
        e.setInputType( android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL |
                android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, 5, 0, 5); page.addView(e, lp);
        return e;
    }

    private void showHome() {
        base("Math Master", "Learn, practise, and build confidence in mathematics.");
        TextView intro = text("Choose a section to get started. Work at your own pace and practise a little every day.", 16, Color.DKGRAY, false);
        intro.setPadding(0, 4, 0, 18); page.addView(intro);
        button("Calculator", this::showCalculator);
        button("Learn the basics", this::showLessons);
        button("Multiplication tables", this::showTables);
        button("Maths formulas", this::showFormulas);
        button("Quick quiz", this::showQuiz);
        page.addView(text("Math Master • Version 1.0", 12, Color.GRAY, false));
    }

    private void backButton() { button("← Home", this::showHome); }

    private void showCalculator() {
        base("Calculator", "Perform basic arithmetic calculations.");
        EditText a = field("First number");
        EditText b = field("Second number");
        TextView result = text("Result will appear here.", 20, navy, true);
        page.addView(result);
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        page.addView(row);
        String[] ops = {"+", "−", "×", "÷"};
        for (String op : ops) {
            Button x = new Button(this); x.setText(op); x.setAllCaps(false);
            x.setOnClickListener(v -> {
                try {
                    double n1 = Double.parseDouble(a.getText().toString());
                    double n2 = Double.parseDouble(b.getText().toString());
                    double r;
                    switch(op) {
                        case "+": r=n1+n2; break;
                        case "−": r=n1-n2; break;
                        case "×": r=n1*n2; break;
                        default:
                            if (n2 == 0) { result.setText("Cannot divide by zero."); return; }
                            r=n1/n2;
                    }
                    result.setText("Result: " + format(r));
                } catch(Exception ex) { result.setText("Enter two valid numbers."); }
            });
            row.addView(x, new LinearLayout.LayoutParams(0, -2, 1));
        }
        backButton();
    }

    private String format(double n) {
        if (n == Math.rint(n)) return String.format(Locale.US, "%.0f", n);
        return String.format(Locale.US, "%.4f", n).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private void showLessons() {
        base("Learn the basics", "Short explanations with worked examples.");
        String[] lessons = {
            "Addition (+)\nAddition combines two or more numbers. Example: 18 + 7 = 25.",
            "Subtraction (−)\nSubtraction finds the difference. Example: 32 − 9 = 23.",
            "Multiplication (×)\nMultiplication is repeated addition. Example: 6 × 4 = 24.",
            "Division (÷)\nDivision shares a number into equal groups. Example: 24 ÷ 6 = 4."
        };
        for (String s : lessons) {
            TextView card = text(s, 17, navy, false);
            card.setBackgroundColor(pale); card.setPadding(14, 14, 14, 14);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            lp.setMargins(0, 6, 0, 8); page.addView(card, lp);
        }
        page.addView(text("Try solving each example yourself, then check the answer.", 14, Color.DKGRAY, false));
        backButton();
    }

    private void showTables() {
        base("Multiplication tables", "Choose a number from 1 to 20.");
        EditText number = field("Enter a number (1–20)");
        TextView output = text("", 18, navy, false); page.addView(output);
        button("Show table", () -> {
            try {
                int n = Integer.parseInt(number.getText().toString());
                if (n < 1 || n > 20) { output.setText("Please enter a number from 1 to 20."); return; }
                StringBuilder s = new StringBuilder();
                for (int i=1;i<=10;i++) s.append(n).append(" × ").append(i).append(" = ").append(n*i).append("\n");
                output.setText(s.toString());
            } catch(Exception e) { output.setText("Enter a whole number first."); }
        });
        backButton();
    }

    private void showFormulas() {
        base("Maths formulas", "A quick reference for common formulas.");
        String[] f = {
            "Rectangle\nArea = length × width\nPerimeter = 2 × (length + width)",
            "Square\nArea = side × side\nPerimeter = 4 × side",
            "Triangle\nArea = ½ × base × height",
            "Circle\nArea = π × radius²\nCircumference = 2 × π × radius",
            "Average\nAverage = sum of values ÷ number of values"
        };
        for (String s : f) {
            TextView card = text(s, 17, navy, false);
            card.setBackgroundColor(pale); card.setPadding(14, 12, 14, 12);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            lp.setMargins(0, 6, 0, 8); page.addView(card, lp);
        }
        backButton();
    }

    private void showQuiz() {
        base("Quick quiz", "Answer a maths question and check your result.");
        final int[] x = {0}, y = {0}, answer = {0}, score = {0}, count = {0};
        TextView question = text("", 22, navy, true);
        TextView feedback = text("Your score: 0", 16, Color.DKGRAY, false);
        page.addView(question); EditText input = field("Your answer"); page.addView(feedback);
        Runnable next = () -> {
            x[0] = random.nextInt(12) + 1; y[0] = random.nextInt(12) + 1;
            int op = random.nextInt(3);
            if (op == 0) { answer[0] = x[0] + y[0]; question.setText(x[0] + " + " + y[0] + " = ?"); }
            else if (op == 1) { if (x[0] < y[0]) { int t=x[0]; x[0]=y[0]; y[0]=t; } answer[0]=x[0]-y[0]; question.setText(x[0]+" − "+y[0]+" = ?"); }
            else { answer[0] = x[0]*y[0]; question.setText(x[0]+" × "+y[0]+" = ?"); }
            input.setText("");
        };
        button("Check answer", () -> {
            try {
                int given = Integer.parseInt(input.getText().toString());
                count[0]++;
                if (given == answer[0]) { score[0]++; feedback.setText("Correct! Score: " + score[0] + " / " + count[0]); }
                else feedback.setText("Not quite. Correct answer: " + answer[0] + ". Score: " + score[0] + " / " + count[0]);
                next.run();
            } catch(Exception e) { feedback.setText("Enter a whole-number answer."); }
        });
        button("New question", next);
        next.run();
        backButton();
    }
}
