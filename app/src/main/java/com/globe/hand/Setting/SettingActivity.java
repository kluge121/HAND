package com.globe.hand.Setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.globe.hand.Main.fragments.FirebaseUserProfileFragment;
import com.globe.hand.common.BaseActivity;
import com.globe.hand.R;
import com.globe.hand.models.JoinedMapRooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setToolbar(R.id.setting_toolbar, true);
        setToolbarTitle("설정");


        getSupportFragmentManager().beginTransaction()
                .add(R.id.setting_profile_container, FirebaseUserProfileFragment.newInstance())
                .commit();

        getFragmentManager().beginTransaction()
                .add(R.id.setting_container, SettingFragment.newInstance())
                .commit();
    }

    public static class SettingFragment extends PreferenceFragment
            implements Preference.OnPreferenceClickListener {
        public SettingFragment() {
            // 기본 퍼블릭 생성자가 필요함
        }

        public static SettingFragment newInstance() {
            return new SettingFragment();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
            findPreference("notice").setOnPreferenceClickListener(this);
            findPreference("question").setOnPreferenceClickListener(this);
            findPreference("logout").setOnPreferenceClickListener(this);
            findPreference("sign_out").setOnPreferenceClickListener(this);
            findPreference("problem").setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "notice":
                    Intent noticeIntent =
                            new Intent(getActivity(), ListForSettingActivity.class);
                    noticeIntent.putExtra("what", "notice");
                    startActivity(noticeIntent);
                    break;
                case "question":
                    Intent questionIntent =
                            new Intent(getActivity(), ListForSettingActivity.class);
                    questionIntent.putExtra("what", "question");
                    startActivity(questionIntent);
                    break;
                case "logout":
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseAuth.getInstance().signOut();
                        if (Session.getCurrentSession().checkAndImplicitOpen()) {
                            UserManagement.requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    ((SettingActivity) getActivity()).redirectLoginActivity();
                                }
                            });
                        }
                        ((SettingActivity) getActivity()).redirectLoginActivity();
                    }
                    break;
                case "sign_out":
                    final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
                    new AlertDialog.Builder(getActivity())
                            .setMessage(appendMessage)
                            .setPositiveButton(getString(R.string.com_kakao_ok_button),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        DocumentReference userReference =
                                                                db.collection("map_room").document(user.getUid());

                                                        deleteCollection(userReference.collection("joined_map_rooms"),
                                                                5, Executors.newCachedThreadPool());
                                                        deleteCollection(userReference.collection("category"),
                                                                5, Executors.newCachedThreadPool());
                                                        deleteCollection(userReference.collection("map_post_ref"),
                                                                5, Executors.newCachedThreadPool());
                                                        userReference.delete();

                                                        db.collection("user").document(user.getUid())
                                                                .delete();
                                                        if (Session.getCurrentSession().checkAndImplicitOpen()) {
                                                            UserManagement.requestUnlink(new UnLinkResponseCallback() {
                                                                @Override
                                                                public void onFailure(ErrorResult errorResult) {
                                                                    Logger.e(errorResult.toString());
                                                                }

                                                                @Override
                                                                public void onSessionClosed(ErrorResult errorResult) {
                                                                    ((SettingActivity) getActivity())
                                                                            .redirectLoginActivity(getActivity(),
                                                                                    errorResult.toString());
                                                                }

                                                                @Override
                                                                public void onNotSignedUp() {
                                                                    // ((SettingActivity) getActivity()).redirectMainActivity();
                                                                }

                                                                @Override
                                                                public void onSuccess(Long userId) {
                                                                    ((SettingActivity) getActivity()).redirectLoginActivity();
                                                                }
                                                            });
                                                        } else {
                                                            ((SettingActivity) getActivity()).redirectLoginActivity();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }).setNegativeButton(getString(R.string.com_kakao_cancel_button),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                case "problem":
                    break;
            }
            return false;
        }

        /**
         * Delete all documents in a collection. Uses an Executor to perform work on a background
         * thread. This does *not* automatically discover and delete subcollections.
         */
        private Task<Void> deleteCollection(final CollectionReference collection,
                                            final int batchSize,
                                            Executor executor) {

            // Perform the delete operation on the provided Executor, which allows us to use
            // simpler synchronous logic without blocking the main thread.
            return Tasks.call(executor, new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    // Get the first batch of documents in the collection
                    Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);

                    // Get a list of deleted documents
                    List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                    // While the deleted documents in the last batch indicate that there
                    // may still be more documents in the collection, page down to the
                    // next batch and delete again
                    while (deleted.size() >= batchSize) {
                        // Move the query cursor to start after the last doc in the batch
                        DocumentSnapshot last = deleted.get(deleted.size() - 1);
                        query = collection.orderBy(FieldPath.documentId())
                                .startAfter(last.getId())
                                .limit(batchSize);

                        deleted = deleteQueryBatch(query);
                    }

                    return null;
                }
            });

        }

        /**
         * Delete all results from a query in a single WriteBatch. Must be run on a worker thread
         * to avoid blocking/crashing the main thread.
         */
        @WorkerThread
        private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
            QuerySnapshot querySnapshot = Tasks.await(query.get());

            WriteBatch batch = query.getFirestore().batch();
            for (DocumentSnapshot snapshot : querySnapshot) {
                batch.delete(snapshot.getReference());
            }
            Tasks.await(batch.commit());

            return querySnapshot.getDocuments();
        }
    }
}
