package com.example.lab_33;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText a;
    EditText b;
    EditText c;
    EditText d;
    EditText y;
    Button calc;
    TextView viewRes;
    EditText mutationCoefficient;

    int yInt;
    int aInt;
    int bInt;
    int cInt;
    int dInt;
    double mutationDouble;

    int[] result = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        y = findViewById(R.id.y);
        calc = findViewById(R.id.calculate_button);
        viewRes = findViewById(R.id.result_text);
        mutationCoefficient = findViewById(R.id.mutationCoefficient);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text1 = y.getText().toString();
                String text2 = a.getText().toString();
                String text3 = b.getText().toString();
                String text4 = c.getText().toString();
                String text5 = d.getText().toString();
                String text6 = mutationCoefficient.getText().toString();
                if (text1.equals("") || text2.equals("") || text3.equals("") || text4.equals("") ||
                        text5.equals("") || text6.equals("")) {
                    Toast.makeText(getApplicationContext(), "Введіть всі дані!", Toast.LENGTH_SHORT).show();
                } else {
                    yInt = Integer.parseInt(text1);
                    aInt = Integer.parseInt(text2);
                    bInt = Integer.parseInt(text3);
                    cInt = Integer.parseInt(text4);
                    dInt = Integer.parseInt(text5);
                    mutationDouble = Double.parseDouble(text6);
                    if (mutationDouble < 0.0 || mutationDouble > 1.0) {
                        Toast.makeText(getApplicationContext(), "Коефіціент мутації" +
                                "повинен бути більше 0 та менше 1!", Toast.LENGTH_SHORT).show();
                    } else {
                        viewRes.setText(geneticAlgorithm());
                    }
                }
            }
        });

    }

    private String geneticAlgorithm() {
        Random rand = new Random();
        int size = 5;
        ArrayList<ArrayList<Integer>> population = new ArrayList<>();
        //fill the first population
        for (int i = 0; i < size; i++) {
            ArrayList<Integer> tmp = new ArrayList<>(4);
            for (int j = 0; j < 4; j++) {
                tmp.add(rand.nextInt(yInt / 2));
            }
            population.add(tmp);
        }

        while (true) {
            double sumDeltas = 0;
            int[] deltas = new int[size];
            //count deltas for all individuals
            for (int i = 0; i < size; i++) {
                deltas[i] = fitnessFunction(population.get(i));
                if (deltas[i] == 0) {
                    return aInt + " * " + population.get(i).get(0) + " + " + bInt + " * " +
                            population.get(i).get(1) + " + " + cInt + " * " + population.get(i).get(2) +
                            " + " + dInt + " * " + population.get(i).get(3) + " = " + yInt;
                }
                sumDeltas += 1.0 / deltas[i];
            }
            //count chance of survival
            double[] parentPercents = new double[size];

            for (int i = 0; i < size; i++) {
                parentPercents[i] = 1.0 / (deltas[i] * sumDeltas) * 100;
            }
            //array of percents for hybridization
            double[] forChildren = new double[size + 1];
            forChildren[0] = 0;

            for (int i = 0; i < size - 1; i++) {
                forChildren[i + 1] = forChildren[i] + parentPercents[i];
            }

            forChildren[size - 1] = 100;
            //choose parents for next generation
            ArrayList<ArrayList<Integer>> parents = new ArrayList<>();

            int forParent;

            for (int i = 0; i < size * 2; i++) {
                forParent = rand.nextInt(100);
                for (int j = 0; j < forChildren.length - 1; j++) {
                    if (forChildren[j] <= forParent && forChildren[j + 1] > forParent) {
                        parents.add(population.get(j));
                    }
                }
            }

            population.clear();
            //create the next generation
            for (int i = 0; i < parents.size(); i = i + 2) {
                ArrayList<Integer> tmp = new ArrayList<>(4);
                forParent = (int)(Math.random() * 3 + 1);
                for (int j = 0; j < forParent; j++) {
                    tmp.add(parents.get(i).get(j));
                }
                for (int j = forParent; j < 4; j++) {
                    tmp.add(parents.get(i + 1).get(j));
                }
                population.add(tmp);
            }

            parents.clear();
            //some mutation
            if (Math.random() < mutationDouble) {
                int countOfChanges = rand.nextInt(size);
                for (int i = 0; i < countOfChanges; i++) {
                    population.get(rand.nextInt(size)).set(rand.nextInt(4),
                            rand.nextInt(yInt / 2));
                }
            }
        }
    }

    private int fitnessFunction(ArrayList<Integer> oneIndividual) {
        return Math.abs(yInt - oneIndividual.get(0) * aInt - oneIndividual.get(1) * bInt -
                oneIndividual.get(2) * cInt - oneIndividual.get(3) * dInt);
    }

}
