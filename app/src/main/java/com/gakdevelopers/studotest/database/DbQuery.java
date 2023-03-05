package com.gakdevelopers.studotest.database;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.gakdevelopers.studotest.models.CategoryModel;
import com.gakdevelopers.studotest.models.Profile;
import com.gakdevelopers.studotest.models.Question;
import com.gakdevelopers.studotest.models.FreeTestsModel;
import com.gakdevelopers.studotest.models.Rank;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {
    public static FirebaseFirestore g_fireStore;

    public static List<CategoryModel> g_catList = new ArrayList<>();

    public static int g_selected_cat_index = 0;

    public static List<FreeTestsModel> g_testList = new ArrayList<>();

    public static int g_selected_test_index = 0;

    public static List<Question> g_question_list = new ArrayList<>();

    public static Profile myProfile = new Profile("NA", null);

    public static Rank myPerformance = new Rank("NULL", 0, -1);

    public static List<Rank> g_users_list = new ArrayList<>();

    public static int getG_selected_cat_index = 0;
    public static int g_positive_marks = 0;
    public static int g_negative_marks = 0;

    public static int g_users_count = 0;

    public static boolean iAmInTopList = false;

    public static void getTopUsers(MyCompleteListener completeListener) {
        g_users_list.clear();

        String myUId = FirebaseAuth.getInstance().getUid();

        g_fireStore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE", 0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            g_users_list.add(new Rank(
                                    doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank
                            ));

                            if (myUId.compareTo(doc.getId()) == 0) {
                                iAmInTopList = true;
                                myPerformance.setRank(rank);
                            }

                            rank++;
                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void getUsersCount(MyCompleteListener completeListener) {
        g_fireStore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        g_users_count = documentSnapshot.getLong("COUNT").intValue();

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void getUserData(final MyCompleteListener completeListener) {
        g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("NAME"));
                        myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));

                        myPerformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());
                        myPerformance.setName(documentSnapshot.getString("NAME"));

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void createUser(String email, String name, final MyCompleteListener completeListener) {
        Map<String, Object> userData = new ArrayMap<>();

        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);

        DocumentReference userDoc = g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_fireStore.batch();
        batch.set(userDoc, userData);

        DocumentReference countDoc = g_fireStore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void updateProfile(String email, String name, final MyCompleteListener completeListener) {
        Map<String, Object> updatedData = new ArrayMap<>();

        updatedData.put("EMAIL_ID", email);
        updatedData.put("NAME", name);

        g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .update(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);
                        myProfile.setEmail(email);

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadCategories(String testType, final MyCompleteListener completeListener) {
        g_catList.clear();

        g_fireStore.collection(testType).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            docList.put(doc.getId(), doc);
                        }

                        QueryDocumentSnapshot catListDoc = docList.get("Categories");

                        long catCount = catListDoc.getLong("COUNT");

                        for (int i  = 1; i <= catCount; i++) {
                            String catId = catListDoc.getString("CAT" + String.valueOf(i) + "_ID");

                            QueryDocumentSnapshot catDoc = docList.get(catId);

                            int noOfTests = catDoc.getLong("NO_OF_TESTS").intValue();
                            String catName = catDoc.getString("NAME");

                            g_catList.add(new CategoryModel(catId, catName, noOfTests));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });


    }

    public static void loadTests(String testType, final MyCompleteListener completeListener) {
        g_testList.clear();

        DocumentReference ref = g_fireStore.collection(testType).document(g_catList.get(g_selected_cat_index).getDocId());

        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                g_positive_marks = value.getLong("POSITIVE").intValue();
                g_negative_marks = value.getLong("NEGATIVE").intValue();
            }
        });

        g_fireStore.collection(testType).document(g_catList.get(g_selected_cat_index).getDocId())
                .collection("TESTS_LIST").document("TEST_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOfTests = g_catList.get(g_selected_cat_index).getNoOfTest();

                        for (int i = 1; i <= noOfTests; i++) {
                            g_testList.add(new FreeTestsModel(
                                    documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue()
                            ));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadData(String testType, final MyCompleteListener completeListener) {
        loadCategories(testType, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        getUsersCount(completeListener);
                    }

                    @Override
                    public void onFailure() {
                        completeListener.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });
    }

    public static void loadQuestions(MyCompleteListener completeListener) {
        g_question_list.clear();

        g_fireStore.collection("QUESTIONS")
                .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDocId())
                .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            g_question_list.add(new Question(
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),
                                    -1,
                                    doc.getString("EXPLANATION")
                            ));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadMyScores(MyCompleteListener completeListener) {
        g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i = 0; i < g_testList.size(); i++) {
                            int top = 0;
                            if (documentSnapshot.get(g_testList.get(i).getTestId()) != null) {
                                top = documentSnapshot.getLong(g_testList.get(i).getTestId()).intValue();
                            }

                            g_testList.get(i).setTopScore(top);
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void saveResult(int score, MyCompleteListener completeListener) {
        WriteBatch batch = g_fireStore.batch();

        DocumentReference userDoc = g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid());

        batch.update(userDoc, "TOTAL_SCORE", score);

        if (score > g_testList.get(g_selected_test_index).getTopScore()) {
            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");

            Map<String, Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_selected_test_index).getTestId(), score);

            batch.set(scoreDoc, testData, SetOptions.merge());
        }

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (score > g_testList.get(g_selected_test_index).getTopScore()) {
                            g_testList.get(g_selected_test_index).setTopScore(score);

                            myPerformance.setScore(score);

                            completeListener.onSuccess();
                        } else {
                            completeListener.onSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }
}
