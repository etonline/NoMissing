package edu.ntust.cs.idsl.nomissing.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
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
import edu.ntust.cs.idsl.nomissing.dao.ChimeDAO;
import edu.ntust.cs.idsl.nomissing.model.Chime;
import edu.ntust.cs.idsl.nomissing.util.ToastMaker;

@SuppressLint("NewApi")
public class ChimeFragment extends ListFragment {
	
	private ChimeDAO chimeDAO;
	private List<Chime> chimeList;
	private ChimeListAdapter adapter;	
	
	private static final int REQUEST_SET = 0;
	public static final int RESULT_CREATE = 1;
	public static final int RESULT_UPDATE = 2;
	public static final int RESULT_CANCEL = 3;
	public static final int RESULT_DELETE = 4;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		chimeDAO = ChimeDAO.getInstance(getActivity());
		chimeList = chimeDAO.findAll();
		
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
			Intent intent = new Intent(getActivity(), SetChimeActivity.class);
			startActivityForResult(intent, REQUEST_SET);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {	
		Intent intent = new Intent(getActivity(), SetChimeActivity.class);
		intent.putExtra("chimeID", (int) id);
		startActivityForResult(intent, REQUEST_SET);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode != REQUEST_SET) return;
		
		chimeList = chimeDAO.findAll();
		adapter = new ChimeListAdapter(getActivity(), chimeList);
		setListAdapter(adapter);
		
		switch (resultCode) {
		case RESULT_CREATE:
			ToastMaker.toast(getActivity(), "建立");
			break;
		case RESULT_UPDATE:
			ToastMaker.toast(getActivity(), "更新");
			break;
		case RESULT_CANCEL:
			ToastMaker.toast(getActivity(), "取消");
			break;
		case RESULT_DELETE:
			ToastMaker.toast(getActivity(), "刪除");
			break;

		default:
			break;
		}
		
	}
	
}
