import { useEffect, useState } from 'react';
import { useUserStore } from '../../../stores';
import { useCookies } from 'react-cookie';
import Navigation from '../../Navigation'
import Authentication from '../../Authentication'
import BoardMain from '../../Navigation'
import axios from 'axios'

export default function MainLayout() {

  const [boardResponse, setBoardResponse] = useState<string>('');
  const [cookies] = useCookies();
  const { user } = useUserStore();

  const getBoard = async (token: string) => {
    const requsetOption = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };

    await axios.get("http://localhost:4000/api/board/", requsetOption).then((response) => {
      setBoardResponse(response.data);
    }).catch((error) => '');
  }

  useEffect(() => {
    const token = cookies.token;
    if(token) getBoard(token);
  }, [user, cookies.token]);

  return (
    <>
        <Navigation />
        {boardResponse ? (<BoardMain />) : (<Authentication />)}
    </>
  )
}
