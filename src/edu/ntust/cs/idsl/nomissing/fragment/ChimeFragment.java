package edu.ntust.cs.idsl.nomissing.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import edu.ntust.cs.idsl.nomissing.R;
import edu.ntust.cs.idsl.nomissing.activity.SetChimeActivity;
import edu.ntust.cs.idsl.nomissing.adapter.ChimeListAdapter;
import edu.ntust.cs.idsl.nomissing.dao.DaoFactory;
import edu.ntust.cs.idsl.nomissing.dao.sqlite.SQLiteDaoFactory;
import edu.ntust.cs.idsl.nomissing.global.Constant;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

/**
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 */
@SuppressLint("NewApi")
public class ChimeFragment extends ListFragment {
	
	private List<Chime> chimeList;
	private ChimeListAdapter adapter;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		chimeList = DaoFactory.getSQLiteDaoFactory().createChimeDao(getActivity()).findAll();
		adapter = new ChimeListAdapter(getActivity(), chimeList);
		setListAdapter(adapter);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chime, container, false);
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_chime, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_chime:
			setChime(0);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {	
		setChime((int)id);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != Constant.REQUEST_CODE_SET) return;
		
		chimeList = DaoFactory.getSQLiteDaoFactory().createChimeDao(getActivity()).findAll();
		adapter = new ChimeListAdapter(getActivity(), chimeList);
		setListAdapter(adapter);
		
		switch (resultCode) {
		case Constant.RESULT_CODE_CREATE:
//			ToastMaker.toast(getActivity(), getString(R.string.toast_create_chime));
			break;
		case Constant.RESULT_CODE_UPDATE:
//			ToastMaker.toast(getActivity(), getString(R.string.toast_update_chime));
			break;
		case Constant.RESULT_CODE_CANCEL:
//			ToastMaker.toast(getActivity(), getString(R.string.toast_cancel_chime));
			break;
		case Constant.RESULT_CODE_DELETE:
//			ToastMaker.toast(getActivity(), getString(R.string.toast_delete_chime));
			break;

		default:
			break;
		}
		
	}
	
	private void setChime(int chimeID) {
		Intent intent = new Intent(getActivity(), SetChimeActivity.class);
		intent.putExtra("chimeID", chimeID);
		startActivityForResult(intent, Constant.REQUEST_CODE_SET);	
	}
	
}
