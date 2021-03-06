/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.sponsoredlinks.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * This class provides Data Access methods for SponsoredLinkGroup objects
 *
 */
public class SponsoredLinkGroupDAO implements ISponsoredLinkGroupDAO
{
    // Constants
    private static final String SQL_QUERY_NEWPK = "SELECT max( id_group ) FROM sponsoredlinks_group ";
    private static final String SQL_QUERY_SELECT = "SELECT id_group, title, tags FROM sponsoredlinks_group WHERE id_group = ? ";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_group, title, tags FROM sponsoredlinks_group ORDER BY title, id_group DESC";
    private static final String SQL_QUERY_INSERT = "INSERT INTO sponsoredlinks_group ( id_group, title, tags )  VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM sponsoredlinks_group WHERE id_group = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE sponsoredlinks_group SET title = ? , tags = ?  WHERE id_group = ? ";
    private static final String SQL_QUERY_SELECT_USED_LIST = "SELECT id_group, title, tags FROM sponsoredlinks_group a WHERE EXISTS ( SELECT 1 FROM sponsoredlinks_set b WHERE a.id_group = b.id_group )";
    private static final String SQL_QUERY_SELECT_UNUSED_LIST = "SELECT id_group, title, tags FROM sponsoredlinks_group a WHERE NOT EXISTS ( SELECT 1 FROM sponsoredlinks_set b WHERE a.id_group = b.id_group )";
    private static final String SQL_QUERY_SELECT_USED = "SELECT id_group, title, tags FROM sponsoredlinks_group a WHERE EXISTS ( SELECT 1 FROM sponsoredlinks_set b WHERE b.id_group = ? )";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Generates a new primary key
     * @param plugin the plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEWPK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    ////////////////////////////////////////////////////////////////////////
    // Methods using a dynamic pool

    /**
     * {@inheritDoc}
     */
    public void insert( SponsoredLinkGroup group, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        group.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, group.getId(  ) );
        daoUtil.setString( 2, group.getTitle(  ) );
        daoUtil.setString( 3, group.getTags(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public SponsoredLinkGroup load( int nGroupId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nGroupId );
        daoUtil.executeQuery(  );

        SponsoredLinkGroup group = null;

        if ( daoUtil.next(  ) )
        {
            group = new SponsoredLinkGroup(  );
            group.setId( daoUtil.getInt( 1 ) );
            group.setTitle( daoUtil.getString( 2 ) );
            group.setTags( daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );

        return group;
    }

    /**
     * {@inheritDoc}
     */
    public void delete( SponsoredLinkGroup group, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, group.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void store( SponsoredLinkGroup group, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setString( 1, group.getTitle(  ) );
        daoUtil.setString( 2, group.getTags(  ) );
        daoUtil.setInt( 3, group.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public Collection<SponsoredLinkGroup> selectAll( Plugin plugin )
    {
        Collection<SponsoredLinkGroup> groupList = new ArrayList<SponsoredLinkGroup>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            SponsoredLinkGroup group = new SponsoredLinkGroup(  );
            group.setId( daoUtil.getInt( 1 ) );
            group.setTitle( daoUtil.getString( 2 ) );
            group.setTags( daoUtil.getString( 3 ) );
            groupList.add( group );
        }

        daoUtil.free(  );

        return groupList;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<SponsoredLinkGroup> selectUsedGroupList( Plugin plugin )
    {
        Collection<SponsoredLinkGroup> groupList = new ArrayList<SponsoredLinkGroup>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USED_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            SponsoredLinkGroup group = new SponsoredLinkGroup(  );
            group.setId( daoUtil.getInt( 1 ) );
            group.setTitle( daoUtil.getString( 2 ) );
            group.setTags( daoUtil.getString( 3 ) );
            groupList.add( group );
        }

        daoUtil.free(  );

        return groupList;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<SponsoredLinkGroup> selectUnusedGroupList( Plugin plugin )
    {
        Collection<SponsoredLinkGroup> groupList = new ArrayList<SponsoredLinkGroup>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_UNUSED_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            SponsoredLinkGroup group = new SponsoredLinkGroup(  );
            group.setId( daoUtil.getInt( 1 ) );
            group.setTitle( daoUtil.getString( 2 ) );
            group.setTags( daoUtil.getString( 3 ) );
            groupList.add( group );
        }

        daoUtil.free(  );

        return groupList;
    }

    /**
     * {@inheritDoc}
     */
    public SponsoredLinkGroup selectUsedGroup( int nGroupId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USED, plugin );
        daoUtil.setInt( 1, nGroupId );
        daoUtil.executeQuery(  );

        SponsoredLinkGroup group = null;

        if ( daoUtil.next(  ) )
        {
            group = new SponsoredLinkGroup(  );
            group.setId( daoUtil.getInt( 1 ) );
            group.setTitle( daoUtil.getString( 2 ) );
            group.setTags( daoUtil.getString( 3 ) );
        }

        return group;
    }
}
