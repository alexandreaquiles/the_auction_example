package unit.ui;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.alexandreaquiles.auctionsniper.Item;
import br.com.alexandreaquiles.auctionsniper.SniperSnapshot;
import br.com.alexandreaquiles.auctionsniper.ui.Column;
import br.com.alexandreaquiles.auctionsniper.ui.SnipersTableModel;
import br.com.alexandreaquiles.auctionsniper.util.Defect;

@RunWith(JMock.class)
public class SnipersTableModelTest {
	private final Mockery context = new Mockery();
	private TableModelListener listener = context.mock(TableModelListener.class);
	private final SnipersTableModel model = new SnipersTableModel();
	
	@Before
	public void attachModelListener(){
		model.addTableModelListener(listener);
	}
	
	@Test
	public void hasEnoughColumns(){
		assertThat(model.getColumnCount(), equalTo(Column.values().length));
	}
	
	@Test
	public void setsSniperValuesInColumns(){
		SniperSnapshot joining = SniperSnapshot.joining(new Item("item-id", 123));
		SniperSnapshot bidding = joining.bidding(555, 666);
		context.checking(new Expectations(){{
			allowing(listener).tableChanged(with(anInsertionEvent()));
			one(listener).tableChanged(with(aChangeInRow(0)));
		}});

		model.addSniperSnapshot(joining);
		model.sniperStateChanged(bidding);
		
		assertRowMatchesSnapshot(0, bidding);
	}
	
	@Test
	public void setsUpColumnHeadings(){
		for (Column column : Column.values()) {
			assertEquals(column.name, model.getColumnName(column.ordinal()));
		}
	}
	
	@Test 
	public void notifiesListenersWhenAddingASniper(){
		SniperSnapshot joining = SniperSnapshot.joining(new Item("item-id", 123));
		context.checking(new Expectations(){{
			one(listener).tableChanged(with(anInsertionAtRow(0)));
		}});
		
		assertEquals(0, model.getRowCount());
		
		model.addSniperSnapshot(joining);
		
		assertEquals(1, model.getRowCount());
		assertRowMatchesSnapshot(0, joining);
	}
	
	@Test 
	public void holdsSnipersInAdditionOrder(){
		context.checking(new Expectations(){{
			ignoring(listener);
		}});
		
		model.addSniperSnapshot(SniperSnapshot.joining(new Item("item 0", 123)));
		model.addSniperSnapshot(SniperSnapshot.joining(new Item("item 1", 345)));
		
		assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER));
		assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER));
	}
	

	@Test
	public void updatesCorrectRowForSniper(){
		context.checking(new Expectations(){{
			ignoring(listener);
		}});
		
		SniperSnapshot joiningItem0 = SniperSnapshot.joining(new Item("item 0", 123));
		model.addSniperSnapshot(joiningItem0);
		SniperSnapshot joiningItem1 = SniperSnapshot.joining(new Item("item 1", 345));
		model.addSniperSnapshot(joiningItem1);
		
		SniperSnapshot biddingItem0 = joiningItem0.bidding(555, 666);
		
		model.sniperStateChanged(biddingItem0);

		assertRowMatchesSnapshot(0, biddingItem0);
		assertRowMatchesSnapshot(1, joiningItem1);
	}

	@Test(expected=Defect.class)
	public void throwsDefectIfNoExistingSniperForAnUpdate(){
		model.sniperStateChanged(SniperSnapshot.joining(new Item("item x", 123)).winning(555));
	}

	private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
		for (Column column : Column.values()) {
			assertEquals(column.valueIn(snapshot), cellValue(row, column));
		}
	}

	private Object cellValue(int row, Column column) {
		return model.getValueAt(row, column.ordinal());
	}

	protected Matcher<TableModelEvent> anInsertionEvent() {
		return hasProperty("type", equalTo(TableModelEvent.INSERT));
	}

	protected Matcher<TableModelEvent> aChangeInRow(int row) {
		return hasProperty("firstRow", equalTo(row));
	}

	protected Matcher<TableModelEvent> anInsertionAtRow(int row) {
		return allOf(anInsertionEvent(), aChangeInRow(row));
	}

}
