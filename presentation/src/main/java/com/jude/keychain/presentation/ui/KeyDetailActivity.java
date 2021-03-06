package com.jude.keychain.presentation.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.data.BeamDataActivity;
import com.jude.keychain.R;
import com.jude.keychain.domain.entities.KeyEntity;
import com.jude.keychain.presentation.presenter.KeyDetailPresenter;
import com.jude.utils.JTimeTransform;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhuchenxi on 15/11/4.
 */
@RequiresPresenter(KeyDetailPresenter.class)
public class KeyDetailActivity extends BeamDataActivity<KeyDetailPresenter, KeyEntity> {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.account)
    TextView account;
    @Bind(R.id.password)
    TextView password;
    @Bind(R.id.note)
    TextView note;
    @Bind(R.id.time)
    TextView time;

    private KeyEntity data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        floatingActionButton.setOnClickListener(v -> {
            Intent i = new Intent(this, AddActivity.class);
            i.putExtra("id", data.getId());
            startActivity(i);
        });
    }

    @Override
    public void setData(KeyEntity data) {
        super.setData(data);
        this.data = data;
        collapsingToolbar.setTitle(data.getName());
        account.setText(data.getAccount());
        password.setText(data.getPassword());
        time.setText(new JTimeTransform(data.getTime()).toString(getString(R.string.date_format)));
        note.setText(TextUtils.isEmpty(data.getNote()) ? getString(R.string.empty) : data.getNote());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {

            new MaterialDialog.Builder(this)
                    .title(R.string.delete)
                    .content(String.format(getString(R.string.confirm_format),data.getName()))
                    .positiveText(R.string.delete)
                    .negativeText(R.string.cancel)
                    .positiveColor(Color.RED)
                    .onPositive((materialDialog, dialogAction) -> getPresenter().delete())
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
